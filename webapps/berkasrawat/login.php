<?php
    include_once "conf/command.php";
    require_once('../conf/conf.php');
    
    $usere          = trim(isset($_GET['usere']))?trim($_GET['usere']):NULL;
    $passwordte     = trim(isset($_GET['passwordte']))?trim($_GET['passwordte']):NULL;
    $keyword        = trim(isset($_GET['keyword']))?trim($_GET['keyword']):NULL;
    $tgl_registrasi = trim(isset($_GET['tgl_registrasi']))?trim($_GET['tgl_registrasi']):NULL;
    
    if ($_GET['act']=="login"){
        if((USERHYBRIDWEB==$usere)&&(PASHYBRIDWEB==$passwordte)){
            session_start();
            $_SESSION['ses_admin_berkas_rawat']="admin";
            
            if ($tgl_registrasi != NULL) {
                $tgl = explode("-", $tgl_registrasi);
                if (count($tgl) == 3) {
                    $tahun = $tgl[0];
                    $bulan = $tgl[1];
                    $tanggal = $tgl[2];
                } else {
                    $tahun = "";
                    $bulan = "";
                    $tanggal = "";
                }
            } else {
                $tahun = "";
                $bulan = "";
                $tanggal = "";
            }
            
            if ($keyword != NULL) {
                $url = "index.php?act=List&iyem=".encrypt_decrypt("{\"tahunawal\":\"".$tahun."\",\"bulanawal\":\"".$bulan."\",\"tanggalawal\":\"".$tanggal."\",\"tahunakhir\":\"".$tahun."\",\"bulanakhir\":\"".$bulan."\",\"tanggalakhir\":\"".$tanggal."\",\"keyword\":\"".$keyword."\"}","e");
            } else {
                $url = "index.php?act=List";			
            }
        }else{
            session_start();
            session_destroy();
            if (cekSessiAdmin()){
                session_unregister("ses_admin_berkas_rawat");
            }
            $url = "index.php?act=HomeAdmin";
        }
        header("Location:".$url);
    }
    
?>
