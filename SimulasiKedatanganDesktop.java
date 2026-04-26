import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SimulasiKedatanganDesktop extends JFrame {
    private JCheckBox opsiLambda2, opsiLambda3, opsiLambda6;
    private JButton tombolMulai, tombolSapuBersih;
    private JLabel labelStatus;
    
    private JTable tabelObservasi;
    private DefaultTableModel modelTabel;
    private JTextArea areaRingkasan;

    public SimulasiKedatanganDesktop() {
        setTitle("Simulator Waktu Antar Kedatangan (Proses Poisson)");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        rakitAntarmuka();
    }

    private void rakitAntarmuka() {
        JPanel panelKendali = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelKendali.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Pusat Kendali Simulasi", TitledBorder.LEFT, TitledBorder.TOP));

        opsiLambda2 = new JCheckBox("λ = 2/menit");
        opsiLambda3 = new JCheckBox("λ = 3/menit");
        opsiLambda6 = new JCheckBox("λ = 6/menit");
        
        tombolMulai = new JButton("Generate Simulasi");
        tombolSapuBersih = new JButton("Clear");
        labelStatus = new JLabel("Status: Menunggu instruksi...");
        labelStatus.setForeground(Color.BLUE);

        tombolMulai.addActionListener(e -> eksekusiSimulasi());
        tombolSapuBersih.addActionListener(e -> bersihkanLayar());

        panelKendali.add(new JLabel("Pilih Arrival Rate (Bisa multi): "));
        panelKendali.add(opsiLambda2);
        panelKendali.add(opsiLambda3);
        panelKendali.add(opsiLambda6);
        panelKendali.add(tombolMulai);
        panelKendali.add(tombolSapuBersih);
        panelKendali.add(labelStatus);

        add(panelKendali, BorderLayout.NORTH);

        String[] kolomTabel = {"No. Kedatangan", "Skenario (λ)", "Inter-arrival Time (menit)"};
        modelTabel = new DefaultTableModel(kolomTabel, 0);
        tabelObservasi = new JTable(modelTabel);
        tabelObservasi.setRowHeight(25);
        
        JScrollPane areaTabel = new JScrollPane(tabelObservasi);
        areaTabel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Log Data Kedatangan", TitledBorder.LEFT, TitledBorder.TOP));
        
        add(areaTabel, BorderLayout.CENTER);

        areaRingkasan = new JTextArea(6, 20);
        areaRingkasan.setEditable(false);
        areaRingkasan.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaRingkasan.setBackground(new Color(245, 245, 245));
        
        JScrollPane areaScrollRingkasan = new JScrollPane(areaRingkasan);
        areaScrollRingkasan.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Ringkasan Analisis", TitledBorder.LEFT, TitledBorder.TOP));

        add(areaScrollRingkasan, BorderLayout.SOUTH);
    }

    private void bersihkanLayar() {
        modelTabel.setRowCount(0);
        areaRingkasan.setText("");
        labelStatus.setText("Status: Layar dibersihkan, siap simulasi baru.");
        labelStatus.setForeground(Color.BLUE);
        opsiLambda2.setSelected(false);
        opsiLambda3.setSelected(false);
        opsiLambda6.setSelected(false);
    }

    private void eksekusiSimulasi() {
        boolean pilih2 = opsiLambda2.isSelected();
        boolean pilih3 = opsiLambda3.isSelected();
        boolean pilih6 = opsiLambda6.isSelected();

        if (!pilih2 && !pilih3 && !pilih6) {
            JOptionPane.showMessageDialog(this, "Pilih minimal satu nilai lambda (λ) dulu, ya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tombolMulai.setEnabled(false);
        tombolSapuBersih.setEnabled(false);
        modelTabel.setRowCount(0);
        areaRingkasan.setText("Memproses data...\n");
        labelStatus.setText("Status: Simulasi sedang berjalan...");
        labelStatus.setForeground(new Color(220, 120, 0)); 

        SwingWorker<String, Object[]> pekerjaSimulasi = new SwingWorker<String, Object[]>() {
            
            @Override
            protected String doInBackground() throws Exception {
                int[] skenarioDipilih = {
                    pilih2 ? 2 : 0, 
                    pilih3 ? 3 : 0, 
                    pilih6 ? 6 : 0
                };
                
                int batasSampel = 15;
                StringBuilder hasilRingkasan = new StringBuilder();

                for (int tingkatKedatangan : skenarioDipilih) {
                    if (tingkatKedatangan == 0) continue; 

                    double akumulasiWaktu = 0.0;

                    for (int n = 1; n <= batasSampel; n++) {
                        double acakU = Math.random();
                        
                        double jeda = -Math.log(1 - acakU) / tingkatKedatangan;
                        akumulasiWaktu += jeda;

                        String jedaFormat = String.format("%.4f", jeda);
                        publish(new Object[]{n, "λ = " + tingkatKedatangan, jedaFormat});
                        
                        Thread.sleep(50); 
                    }

                    double rataRata = akumulasiWaktu / batasSampel;
                    hasilRingkasan.append(String.format("► Skenario λ = %d kedatangan/menit\n", tingkatKedatangan));
                    hasilRingkasan.append(String.format("   Total Waktu (%d kedatangan) : %.4f menit\n", batasSampel, akumulasiWaktu));
                    hasilRingkasan.append(String.format("   Rata-rata Inter-arrival Time : %.4f menit\n\n", rataRata));
                }
                
                return hasilRingkasan.toString();
            }

            @Override
            protected void process(List<Object[]> barisanData) {
                for (Object[] baris : barisanData) {
                    modelTabel.addRow(baris);
                    tabelObservasi.scrollRectToVisible(tabelObservasi.getCellRect(modelTabel.getRowCount()-1, 0, true));
                }
            }

            @Override
            protected void done() {
                try {
                    String laporanAkhir = get();
                    areaRingkasan.setText(laporanAkhir);
                    
                    labelStatus.setText("Status: Selesai! Menunggu instruksi selanjutnya.");
                    labelStatus.setForeground(new Color(46, 139, 87)); // Hijau sukses
                    tombolMulai.setEnabled(true);
                    tombolSapuBersih.setEnabled(true);
                } catch (Exception e) {
                    labelStatus.setText("Status: Terjadi kesalahan simulasi.");
                }
            }
        };

        pekerjaSimulasi.execute(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimulasiKedatanganDesktop aplikasi = new SimulasiKedatanganDesktop();
            aplikasi.setVisible(true);
        });
    }
}