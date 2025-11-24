package gui;

import interfaces.FuelConsumable;
import managers.FleetManager;
import vehicles.Vehicle;
import simulation.VehicleController;
import simulation.SharedHighwayCounter;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;


public class FleetSimulatorGUI extends JFrame {
    private final FleetManager fleetManager;
    private final JPanel listPanel = new JPanel();
    private final JLabel counterUnsafe = new JLabel("Unsafe counter: 0");
    private final JLabel counterSafe = new JLabel("Safe counter: 0");
    private final JLabel counterSum = new JLabel("Sum of mileages: 0.00");
    private final JLabel counterDiff = new JLabel("Difference (unsafe - sum): 0.00");
    private final JButton startBtn = new JButton("Start");
    private final JButton pauseBtn = new JButton("Pause All");
    private final JButton resumeBtn = new JButton("Resume All");
    private final JButton stopBtn = new JButton("Stop All");
    private final JCheckBox safeToggle = new JCheckBox("Use Safe Counter (Lock)");
    // controllers keyed by vehicle id (preserves insertion order)
    private final Map<String, VehicleController> controllers = new LinkedHashMap<>();
    // mapping vehicle id -> JLabel that displays that vehicle row (for safe updates)
    private final Map<String, JLabel> vehicleLabels = new HashMap<>();
    private Timer uiTimer;

    public FleetSimulatorGUI(FleetManager manager) {
        super("Fleet Highway Simulator");
        this.fleetManager = manager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        buildUI();
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(startBtn);
        top.add(safeToggle);
        top.add(pauseBtn);
        top.add(resumeBtn);
        top.add(stopBtn);
        add(top, BorderLayout.NORTH);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane sp = new JScrollPane(listPanel);
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bottom.add(counterUnsafe);
        bottom.add(counterSafe);
        bottom.add(counterSum);
        bottom.add(counterDiff);
        add(bottom, BorderLayout.SOUTH);

        // Actions
        startBtn.addActionListener(e -> startSimulation());
        pauseBtn.addActionListener(e -> controllers.values().forEach(VehicleController::pause));
        resumeBtn.addActionListener(e -> controllers.values().forEach(VehicleController::resume));
        stopBtn.addActionListener(e -> stopSimulation());
        safeToggle.addActionListener(e -> setSafeModeForControllers(safeToggle.isSelected()));

        // populate vehicle rows
        List<Vehicle> fleet = loadVehicles();
        if (fleet.isEmpty()) {
            JLabel info = new JLabel("No vehicles available in FleetManager. Add vehicles or call demo setup before launching GUI.");
            info.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
            listPanel.add(info);
        } else {
            for (Vehicle v : fleet) {
                addVehicleRow(v);
            }
        }

        // UI refresh timer runs on EDT and updates labels safely
        uiTimer = new Timer(500, ev -> refreshUI());
        uiTimer.start();
    }

    private List<Vehicle> loadVehicles() {
        try {
            List<Vehicle> list = fleetManager.getAllVehicles();
            return list != null ? list : Collections.emptyList();
        } catch (Throwable t) {
            return Collections.emptyList();
        }
    }

    private void addVehicleRow(Vehicle v) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        JLabel info = new JLabel(formatVehicle(v));
        info.setPreferredSize(new Dimension(600, 20));
        JButton refuel = new JButton("Refuel +100");
        refuel.addActionListener(a -> {
            try {
                ((FuelConsumable) v).refuel(100.0);
                VehicleController ctrl = controllers.get(v.getId());
                if (ctrl != null) ctrl.resume();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Refuel failed: " + ex.getMessage());
            }
        });
        row.add(info);
        row.add(refuel);
        listPanel.add(row);
        vehicleLabels.put(v.getId(), info);
    }

    private String formatVehicle(Vehicle v) {
        String fuelStr = "?";
        try {
            fuelStr = String.format("%.2f", ((FuelConsumable) v).getFuelLevel());
        } catch (Throwable ignored) {}
        return String.format("%s | Model: %s | Mileage: %.2f | Fuel: %s",
                v.getId(), v.getModel(), v.getCurrentMileage(), fuelStr);
    }

    private void startSimulation() {
        // reset counters for clean demo
        SharedHighwayCounter.resetAll();
        boolean useSafe = safeToggle.isSelected();

        // stop existing controllers if already running
        if (!controllers.isEmpty()) {
            controllers.values().forEach(VehicleController::stop);
            controllers.clear();
        }

        // ensure UI labels correspond to current fleet (reload if needed)
        List<Vehicle> fleet = loadVehicles();
        // If the fleet has changed (IDs different), rebuild the listPanel
        if (!vehicleLabels.keySet().equals(new LinkedHashSet<>(mapIds(fleet)))) {
            // rebuild UI rows
            vehicleLabels.clear();
            listPanel.removeAll();
            for (Vehicle v : fleet) addVehicleRow(v);
            listPanel.revalidate();
            listPanel.repaint();
        }

        // create controllers and start threads
        for (Vehicle v : fleet) {
            VehicleController ctrl = new VehicleController(v, useSafe);
            controllers.put(v.getId(), ctrl);
            ctrl.start();
        }

        startBtn.setEnabled(false);
    }

    private List<String> mapIds(List<Vehicle> fleet) {
        List<String> ids = new ArrayList<>();
        for (Vehicle v : fleet) ids.add(v.getId());
        return ids;
    }

    private void stopSimulation() {
        controllers.values().forEach(VehicleController::stop);
        controllers.clear();
        startBtn.setEnabled(true);
    }

    private void setSafeModeForControllers(boolean useSafe) {
        controllers.values().forEach(c -> c.setUseSafeCounter(useSafe));
    }

    private void refreshUI() {
        // Update counters
        counterUnsafe.setText("Unsafe counter: " + SharedHighwayCounter.getUnsafe());
        counterSafe.setText("Safe counter: " + SharedHighwayCounter.getSafe());

        // Sum mileage across vehicles, update vehicle labels
        double sum = 0.0;
        for (Vehicle v : loadVehicles()) {
            sum += v.getCurrentMileage();
            JLabel lbl = vehicleLabels.get(v.getId());
            if (lbl != null) lbl.setText(formatVehicle(v));
        }
        counterSum.setText(String.format("Sum of mileages: %.2f", sum));
        double diff = SharedHighwayCounter.getUnsafe() - sum;
        counterDiff.setText(String.format("Difference (unsafe - sum): %.2f", diff));
    }

    // Launch helper
    public static void launch(FleetManager manager) {
        SwingUtilities.invokeLater(() -> {
            FleetSimulatorGUI gui = new FleetSimulatorGUI(manager);
            gui.setVisible(true);
        });
    }
}