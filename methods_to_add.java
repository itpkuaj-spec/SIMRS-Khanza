    private void mnJadikanTidakKlaimActionPerformed(java.awt.event.ActionEvent evt) {
        widget.Table tb = TabRawat.getSelectedIndex() == 1 ? tbListPasienRanap : tbListPasienRajal;
        if (tb.getSelectedRow() != -1) {
            noRawatCatatan = tb.getValueAt(tb.getSelectedRow(), 1).toString().trim();
            modeCatatanTidakKlaim = true;
            String cat = Sequel.cariIsi("select catatan from pku_list_klaim where no_rawat='" + noRawatCatatan + "'");
            catatanPerbaikan1.setText(cat == null ? "" : cat);
            DlgCatatanPerbaikan.setSize(500, 250);
            DlgCatatanPerbaikan.setLocationRelativeTo(internalFrame1);
            DlgCatatanPerbaikan.setVisible(true);
        }
    }

    private void tampilTidakKlaim() {
        int countSep = 0;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienTidakKlaim);
            String shortdokter2;
            if (kdDokterView.getText().equals("")) {
                shortdokter2 = " ";
            } else {
                shortdokter2 = " reg_periksa.kd_dokter='" + kdDokterView.getText() + "' and ";
            }
            sql = "select reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, poliklinik.nm_poli, "
                + "dokter.nm_dokter, bridging_sep.no_sep, bridging_sep.tglsep, reg_periksa.tgl_registrasi, "
                + "pku_list_klaim.catatan, pku_list_klaim.tgl_input, pku_list_klaim.user_input "
                + "from reg_periksa "
                + "JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli "
                + "JOIN dokter ON reg_periksa.kd_dokter=dokter.kd_dokter "
                + "LEFT JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat "
                + "JOIN pku_list_klaim ON reg_periksa.no_rawat=pku_list_klaim.no_rawat "
                + "where " + shortdokter2 + " status_lanjut='Ralan' and reg_periksa.kd_pj='BPJ' "
                + "and reg_periksa.stts<>'Batal' and pku_list_klaim.status_tidak_klaim='1' "
                + "and reg_periksa.tgl_registrasi BETWEEN ? and ? "
                + "and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ?) ";
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    TabModePasienTidakKlaim.addRow(new Object[]{
                        false,
                        rs.getString("no_rawat"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("nm_poli"),
                        rs.getString("nm_dokter"),
                        (rs.getString("no_sep") == null ? "-" : rs.getString("no_sep")),
                        (rs.getString("tglsep") == null ? "-" : rs.getString("tglsep")),
                        rs.getString("tgl_registrasi"),
                        (rs.getString("catatan") == null ? "" : rs.getString("catatan")),
                        (rs.getString("tgl_input") == null ? "-" : rs.getString("tgl_input")),
                        (rs.getString("user_input") == null ? "-" : rs.getString("user_input"))
                    });
                }
            } catch (Exception ex) {
                System.out.println("tampilTidakKlaim: " + ex);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("tampilTidakKlaim: " + e);
        }
        LCount.setText("" + tbListPasienTidakKlaim.getRowCount());
        SepTerbit.setText("" + countSep);
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilRanapTidakKlaim() {
        int countSep = 0;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienRanapTidakKlaim);
            String shortdokter2;
            if (kdDokterView.getText().equals("")) {
                shortdokter2 = " ";
            } else {
                shortdokter2 = " reg_periksa.kd_dokter='" + kdDokterView.getText() + "' and ";
            }
            sql = "select reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, "
                + "date(bridging_sep.tglpulang) as tgl_pulang, bridging_sep.no_sep, bridging_sep.tglsep, reg_periksa.tgl_registrasi, "
                + "pku_list_klaim.catatan, pku_list_klaim.tgl_input, pku_list_klaim.user_input "
                + "from reg_periksa "
                + "JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "LEFT JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat "
                + "JOIN pku_list_klaim ON reg_periksa.no_rawat=pku_list_klaim.no_rawat "
                + "where " + shortdokter2 + " status_lanjut='Ranap' and reg_periksa.kd_pj='BPJ' "
                + "and reg_periksa.stts<>'Batal' and pku_list_klaim.status_tidak_klaim='1' "
                + "and reg_periksa.tgl_registrasi BETWEEN ? and ? "
                + "and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ?) ";
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("no_sep") != null && !rs.getString("no_sep").trim().isEmpty() && !rs.getString("no_sep").trim().equals("-")) {
                        countSep++;
                    }
                    String tglCheckout = "", kamar = "", dpjp = "";
                    kamar = Sequel.cariIsi("select concat(kamar_inap.kd_kamar,', ',bangsal.nm_bangsal,', ',kamar.kelas) from kamar_inap inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat='" + rs.getString("no_rawat") + "' order by kamar_inap.tgl_keluar DESC limit 1");
                    dpjp = Sequel.cariIsi("select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='" + rs.getString("no_rawat") + "'");
                    tglCheckout = Sequel.cariIsi("select tgl_keluar from kamar_inap where no_rawat='" + rs.getString("no_rawat") + "' and ( stts_pulang!='-' or stts_pulang!='Pindah Kamar')  ORDER BY tgl_keluar desc limit 1 ");

                    TabModePasienRanapTidakKlaim.addRow(new Object[]{
                        false,
                        rs.getString("no_rawat"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        kamar,
                        dpjp,
                        (rs.getString("no_sep") == null ? "-" : rs.getString("no_sep")),
                        (rs.getString("tglsep") == null ? "-" : rs.getString("tglsep")),
                        rs.getString("tgl_registrasi"),
                        (tglCheckout == null || tglCheckout.equals("") ? "-" : tglCheckout),
                        (rs.getString("catatan") == null ? "" : rs.getString("catatan")),
                        (rs.getString("tgl_input") == null ? "-" : rs.getString("tgl_input")),
                        (rs.getString("user_input") == null ? "-" : rs.getString("user_input"))
                    });
                }
            } catch (Exception ex) {
                System.out.println("tampilRanapTidakKlaim: " + ex);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("tampilRanapTidakKlaim: " + e);
        }
        LCount.setText("" + tbListPasienRanapTidakKlaim.getRowCount());
        SepTerbit.setText("" + countSep);
        this.setCursor(Cursor.getDefaultCursor());
    }
}
