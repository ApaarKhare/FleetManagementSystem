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

    // Controller threads for vehicles
    private final Map<String, VehicleController> controllers = new LinkedHashMap<>();
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

        // --- TOP PANEL (Buttons) ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(startBtn);
        top.add(safeToggle);
        top.add(pauseBtn);
        top.add(resumeBtn);
        top.add(stopBtn);
        add(top, BorderLayout.NORTH);

        // --- CENTER PANEL: List of Vehicles ---
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane sp = new JScrollPane(listPanel);
        add(sp, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Counters) ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        bottom.add(counterUnsafe);
        bottom.add(counterSafe);
        bottom.add(counterSum);
        bottom.add(counterDiff);
        add(bottom, BorderLayout.SOUTH);

        // --- Actions ---
        startBtn.addActionListener(e -> startSimulation());
        pauseBtn.addActionListener(e -> controllers.values().forEach(VehicleController::pause));
        resumeBtn.addActionListener(e -> controllers.values().forEach(VehicleController::resume));
        stopBtn.addActionListener(e -> stopSimulation());
        safeToggle.addActionListener(e ->
                controllers.values().forEach(c -> c.setUseSafeCounter(safeToggle.isSelected()))
        );

        // --- Build vehicle rows ---
        List<Vehicle> fleet = loadVehicles();
        if (fleet.isEmpty()) {
            JLabel info = new JLabel("No vehicles found in FleetManager.");
            info.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            listPanel.add(info);
        } else {
            for (Vehicle v : fleet) addVehicleRow(v);
        }

        // --- Start UI updater ---
        uiTimer = new Timer(500, ev -> refreshUI());
        uiTimer.start();
    }

    private List<Vehicle> loadVehicles() {
        try {
            List<Vehicle> list = fleetManager.getAllVehicles();
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void addVehicleRow(Vehicle v) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JLabel info = new JLabel(formatVehicle(v));
        info.setPreferredSize(new Dimension(620, 22));

        JButton refuel = new JButton("Refuel +100");
        refuel.addActionListener(a -> {
            try {
                if (!(v instanceof FuelConsumable)) {
                    JOptionPane.showMessageDialog(this, "Vehicle " + v.getId() + " is not fuel-consumable.");
                    return;
                }
                ((FuelConsumable) v).refuel(100.0);

                VehicleController ctrl = controllers.get(v.getId());
                if (ctrl != null) ctrl.resume();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Refuel failed: " + ex.getMessage(),
                        "Refuel Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        row.add(info);
        row.add(refuel);

        listPanel.add(row);
        vehicleLabels.put(v.getId(), info);
    }

    private String formatVehicle(Vehicle v) {
        String fuel = "?";
        try {
            fuel = String.format("%.2f", ((FuelConsumable) v).getFuelLevel());
        } catch (Exception ignored) {}

        return String.format("%s  |  %s  |  Mileage: %.2f km  |  Fuel: %s",
                v.getId(), v.getModel(), v.getCurrentMileage(), fuel);
    }

    private void startSimulation() {

        SharedHighwayCounter.resetAll();
        boolean safe = safeToggle.isSelected();

        // stop old controllers
        controllers.values().forEach(VehicleController::stop);
        controllers.clear();

        // rebuild rows (if vehicles changed)
        List<Vehicle> fleet = loadVehicles();
        listPanel.removeAll();
        vehicleLabels.clear();
        for (Vehicle v : fleet) addVehicleRow(v);
        listPanel.revalidate();
        listPanel.repaint();

        // start new controllers
        for (Vehicle v : fleet) {
            VehicleController ctrl = new VehicleController(v, safe);
            ctrl.start();
            controllers.put(v.getId(), ctrl);
        }

        startBtn.setEnabled(false);
    }

    private void stopSimulation() {
        controllers.values().forEach(VehicleController::stop);
        controllers.clear();
        startBtn.setEnabled(true);
    }

    private void refreshUI() {

        counterUnsafe.setText("Unsafe counter: " + SharedHighwayCounter.getUnsafe());
        counterSafe.setText("Safe counter: " + SharedHighwayCounter.getSafe());

        double sum = 0.0;
        List<Vehicle> fleet = loadVehicles();

        for (Vehicle v : fleet) {
            sum += v.getCurrentMileage();

            JLabel lbl = vehicleLabels.get(v.getId());
            if (lbl != null) lbl.setText(formatVehicle(v));
        }

        counterSum.setText(String.format("Sum: %.2f", sum));

        if (safeToggle.isSelected()){
            counterDiff.setText(String.format("Difference: %.2f", sum- SharedHighwayCounter.getSafe()));
        }
        else{
            counterDiff.setText(String.format("Difference: %.2f", sum- SharedHighwayCounter.getUnsafe()));
        }

    }

    public static void launch(FleetManager manager) {
        SwingUtilities.invokeLater(() -> new FleetSimulatorGUI(manager).setVisible(true));
    }
}