<?php
require_once('../conf/conf.php');
require_once('config_akun.php');

function mc_encrypt($data, $strkey)
{
    $key = hex2bin($strkey);
    if (mb_strlen($key, "8bit") !== 32) {
        throw new Exception("Needs a 256-bit key!");
    }

    $iv_size = openssl_cipher_iv_length("aes-256-cbc");
    $iv = openssl_random_pseudo_bytes($iv_size);
    $encrypted = openssl_encrypt($data, "aes-256-cbc", $key, OPENSSL_RAW_DATA, $iv);
    $signature = mb_substr(hash_hmac("sha256", $encrypted, $key, true), 0, 10, "8bit");
    $encoded = chunk_split(base64_encode($signature . $iv . $encrypted));
    return $encoded;
}

function mc_decrypt($str, $strkey)
{
    $key = hex2bin($strkey);
    if (mb_strlen($key, "8bit") !== 32) {
        throw new Exception("Needs a 256-bit key!");
    }

    $iv_size = openssl_cipher_iv_length("aes-256-cbc");
    $decoded = base64_decode($str);
    $signature = mb_substr($decoded, 0, 10, "8bit");
    $iv = mb_substr($decoded, 10, $iv_size, "8bit");
    $encrypted = mb_substr($decoded, $iv_size + 10, NULL, "8bit");
    $calc_signature = mb_substr(hash_hmac("sha256", $encrypted, $key, true), 0, 10, "8bit");
    if (!mc_compare($signature, $calc_signature)) {
        return "SIGNATURE_NOT_MATCH";
    }

    $decrypted = openssl_decrypt($encrypted, "aes-256-cbc", $key, OPENSSL_RAW_DATA, $iv);
    return $decrypted;
}

function mc_compare($a, $b)
{
    if (strlen($a) !== strlen($b)) {
        return false;
    }

    $result = 0;

    for ($i = 0; $i < strlen($a); $i++) {
        $result |= ord($a[$i]) ^ ord($b[$i]);
    }

    return $result == 0;
}

function CreateClaim($nomor_kartu, $nomor_sep, $nomor_rm, $nama_pasien, $tgl_lahir, $gender, $norawat)
{
    // 1 lakilaki,2 perempuan
    switch ($gender) {
        case "L":
            $genderCnv = 1;
            break;
        case "P":
            $genderCnv = 2;
            break;
        default:
            $genderCnv = 1;
    }
    $request = '{
                        "metadata":{
                            "method":"new_claim"
                        },
                        "data":{
                            "nomor_kartu":"' . $nomor_kartu . '",
                            "nomor_sep":"' . $nomor_sep . '",
                            "nomor_rm":"' . $nomor_rm . '",
                            "nama_pasien":"' . $nama_pasien . '",
                            "tgl_lahir":"' . $tgl_lahir . '",
                            "gender":"' . $genderCnv . '"
                        }
                    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function setDataClaim(
    $nomor_sep,
    $tgl_masuk,
    $tgl_pulang,
    $cara_masuk,
    $jenis_rawat,
    $kelas_rawat,
    $adl_sub_acute,
    $adl_chronic,
    $icu_indikator,
    $icu_los,
    $use_ind,
    $start_dttm,
    $stop_dttm,
    $diagnosa,
    $procedure,
    $nama_dokter,
    $coder_nik,
    $konsultasi = 0,
    $lab = 0,
    $obat = 0,
    $non_bedah = 0,
    $bedah = 0,
    $keperawatan = 0,
    $radiologi = 0,
    $upgrade_class_ind = "",
    $upgrade_class_class = "",
    $upgrade_class_los = "",
    $kamar = 0
) {
    $request = '{
                        "metadata": {
                            "method": "set_claim_data",
                            "nomor_sep": "' . $nomor_sep . '"
                        },
                        "data": {
                            "nomor_sep": "' . $nomor_sep . '",
                            "nomor_kartu": "",
                            "tgl_masuk": "' . $tgl_masuk . '",
                            "tgl_pulang": "' . $tgl_pulang . '",
                            "cara_masuk": "' . $cara_masuk . '",
                            "jenis_rawat": "' . $jenis_rawat . '",
                            "kelas_rawat": "' . $kelas_rawat . '",
                            "adl_sub_acute": "' . $adl_sub_acute . '",
                            "adl_chronic": "' . $adl_chronic . '",
                            "icu_indikator": "' . $icu_indikator . '",
                            "icu_los": "' . $icu_los . '",
                            "ventilator_hour": {
                                "use_ind": "' . $use_ind . '",
                                "start_dttm": "' . $start_dttm . '",
                                "stop_dttm": "' . $stop_dttm . '"
                                            },
                            "upgrade_class_ind": "' . $upgrade_class_ind . '",
                            "upgrade_class_class": "' . $upgrade_class_class . '",
                            "upgrade_class_los": "' . $upgrade_class_los . '",
                            "add_payment_pct": "",
                            "birth_weight": "",
                            "discharge_status": "",
                            "diagnosa": "' . $diagnosa . '",
                            "procedure": "' . $procedure . '",
                            "tarif_rs": {
                                "prosedur_non_bedah": "' . $non_bedah . '",
                                "prosedur_bedah": "' . $bedah . '",
                                "konsultasi": "' . $konsultasi . '",
                                "tenaga_ahli": "0",
                                "keperawatan": "' . $keperawatan . '",
                                "penunjang": "0",
                                "radiologi": "' . $radiologi . '",
                                "laboratorium": "' . $lab . '",
                                "pelayanan_darah": "0",
                                "rehabilitasi": "0",
                                "kamar": "' . $kamar . '",
                                "rawat_intensif": "0",
                                "obat": "' . $obat . '",
                                "obat_kronis": "0",
                                "obat_kemoterapi": "0",
                                "alkes": "0",
                                "bmhp": "0",
                                "sewa_alat": "0"
                             },
                            "tarif_poli_eks": "0",
                            "nama_dokter": "' . $nama_dokter . '",
                            "kode_tarif": "' . getKelasRS() . '",
                            "payor_id": "3",
                            "payor_cd": "JKN",
                            "cob_cd": "",
                            "coder_nik": "' . $coder_nik . '"
                        }
                   }';
    // echo "Data : ".$request;
    $msg = Request($request);
    return $msg;
    // echo "Response Data : ".json_encode($msg);


    //     // Hapus2("inacbg_data_terkirim", "no_sep='".$nomor_sep."'");
    //     // InsertData2("inacbg_data_terkirim","'".$nomor_sep."','".$coder_nik."'");
    //     // GroupingStage1($nomor_sep,$coder_nik);
    // }
}
function setDiagnosaIDRG($nomor_sep, $diagnosa)
{
    $request = '{
        "metadata": {
        "method": "idrg_diagnosa_set",
        "nomor_sep": "' . $nomor_sep . '"
        },
        "data": {
            "diagnosa": "' . $diagnosa . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function setProcedureIDRG($nomor_sep, $prosedure)
{
    $request = '{
        "metadata": {
        "method": "idrg_procedure_set",
        "nomor_sep": "' . $nomor_sep . '"
        },
        "data": {
            "procedure": "' . $prosedure . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function setGrouperIDRG($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "grouper",
            "stage": "1",
            "grouper": "idrg"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function getCmg($nomor_sep, $special_cmg)
{
    $request = '{
        "metadata": {
            "method": "grouper",
            "stage": "2",
            "grouper": "inacbg"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '",
            "special_cmg": "' . $special_cmg . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}

function finalGrouperIDRG($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "idrg_grouper_final"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function editGrouperIDRG($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "idrg_grouper_reedit"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function importCoding($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "idrg_to_inacbg_import"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function setFinalClaim($nomor_sep, $coder_nik)
{
    $request = '{
        "metadata": {
            "method": "claim_final"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '",
            "coder_nik":"' . $coder_nik . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function editClaim($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "reedit_claim"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';

    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function sendClaim($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "send_claim_individual"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function getClaim($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "get_claim_data"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}

function setDiagnosaINACBG($nomor_sep, $diagnosa)
{
    $request = '{
        "metadata": {
        "method": "inacbg_diagnosa_set",
        "nomor_sep": "' . $nomor_sep . '"
        },
        "data": {
            "diagnosa": "' . $diagnosa . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}


function setProcedureINACBG($nomor_sep, $prosedure)
{
    $request = '{
        "metadata": {
        "method": "inacbg_procedure_set",
        "nomor_sep": "' . $nomor_sep . '"
        },
        "data": {
            "procedure": "' . $prosedure . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function setGrouperINACBGStage1($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "grouper",
            "stage": "1",
            "grouper": "inacbg"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}
function setFinalINACBG($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "inacbg_grouper_final"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}

function setEditINACBG($nomor_sep)
{
    $request = '{
        "metadata": {
            "method": "inacbg_grouper_reedit"
        },
        "data": {
            "nomor_sep": "' . $nomor_sep . '"
        }
    }';
    $msg = Request($request);
    // if ($msg['metadata']['message'] == "Ok") {
    //     InsertData2("inacbg_klaim_baru2", "'" . $norawat . "','" . $nomor_sep . "','" . $msg['response']['patient_id'] . "','" . $msg['response']['admission_id'] . "','" . $msg['response']['hospital_admission_id'] . "'");
    // }
    return $msg;
}


function setClaim(
    $nomor_sep,
    $nomor_kartu,
    $tgl_masuk,
    $tgl_pulang,
    $cara_masuk,
    $jenis_rawat,
    $kelas_rawat,
    $sistole,
    $diastole,
    $nama_dokter,

    $adl_sub_acute,
    $adl_chronic,
    $icu_indikator,
    $icu_los,
    $use_ind,
    $start_dttm,
    $stop_dttm,
    $coder_nik,
    $birth_weight = 0,
    $konsultasi = 0,
    $lab = 0,
    $obat = 0,
    $non_bedah = 0,
    $bedah = 0,
    $keperawatan = 0,
    $radiologi = 0,
    $tfkamar = 0,
    $tenaga_ahli = 0,
    $penunjang = 0,
    $pelayanan_darah = 0,
    $rehabilitasi = 0,
    $rawat_intensif = 0,
    $obat_kronis = 0,
    $alkes = 0,
    $bmhp = 0,
    $sewa_alat = 0,
    $upgrade_class_ind = "",
    $upgrade_class_class = "",
    $upgrade_class_los = "",
    $tarif_poli_eks = 0
) {
    $request = '{
                        "metadata": {
                            "method": "set_claim_data",
                            "nomor_sep": "' . $nomor_sep . '"
                        },
                        "data": {
                            "nomor_sep": "' . $nomor_sep . '",
                            "nomor_kartu": "' . $nomor_kartu . '",
                            "tgl_masuk": "' . $tgl_masuk . '",
                            "tgl_pulang": "' . $tgl_pulang . '",
                            "cara_masuk": "' . $cara_masuk . '",
                            "jenis_rawat": "' . $jenis_rawat . '",
                            "kelas_rawat": "' . $kelas_rawat . '",
                             "adl_sub_acute": "' . $adl_sub_acute . '",
                            "adl_chronic": "' . $adl_chronic . '",
                            "icu_indikator": "' . $icu_indikator . '",
                            "icu_los": "' . $icu_los . '",
                            "ventilator": {
                                "use_ind": "' . $use_ind . '",
                                "start_dttm": "' . $start_dttm . '",
                                "stop_dttm": "' . $stop_dttm . '"
                                            },
                            "upgrade_class_ind": "' . $upgrade_class_ind . '",
                            "upgrade_class_class": "' . $upgrade_class_class . '",
                            "upgrade_class_los": "' . $upgrade_class_los . '",
                            "add_payment_pct": "10",
                            "birth_weight": "' . $birth_weight . '",
                            "sistole": ' . $sistole . ',
                            "diastole": ' . $diastole . ',
                            "discharge_status": "1",
                            "tarif_rs": {
                                "prosedur_non_bedah": "' . $non_bedah . '",
                                "prosedur_bedah": "' . $bedah . '",
                                "konsultasi": "' . $konsultasi . '",
                                "tenaga_ahli": "' . $tenaga_ahli . '",
                                "keperawatan": "' . $keperawatan . '",
                                "penunjang": "' . $penunjang . '",
                                "radiologi": "' . $radiologi . '",
                                "laboratorium": "' . $lab . '",
                                "pelayanan_darah": "' . $pelayanan_darah . '",
                                "rehabilitasi": "' . $rehabilitasi . '",
                                "kamar": "' . $tfkamar . '",
                                "rawat_intensif": "' . $rawat_intensif . '",
                                "obat": "' . $obat . '",
                                "obat_kronis": "' . $obat_kronis . '",
                                "obat_kemoterapi": "' . $bedah . '",
                                "alkes": "' . $alkes . '",
                                "bmhp": "' . $bmhp . '",
                                "sewa_alat": "' . $sewa_alat . '"
                            },
                            "pemulasaraan_jenazah": "1",
                            "kantong_jenazah": "1",
                            "peti_jenazah": "1",
                            "plastik_erat": "1",
                            "desinfektan_jenazah": "1",
                            "mobil_jenazah": "0",
                            "desinfektan_mobil_jenazah": "0",
                            "covid19_status_cd": "1",
                            "nomor_kartu_t": "nik",
                            "episodes": "1;12#2;3#6;5",
                            "akses_naat": "C",
                            "isoman_ind": "0",
                            "bayi_lahir_status_cd": 1,
                            "dializer_single_use": "1",
                            "kantong_darah": 1,
                            "alteplase_ind": 1,
                            "tarif_poli_eks": "' . $tarif_poli_eks . '",
                            "nama_dokter": "' . $nama_dokter . '",
                            "kode_tarif": "' . getKelasRS() . '",
                            "payor_id": "3",
                            "payor_cd": "JKN",
                            "cob_cd": 0,
                            "coder_nik": "' . $coder_nik . '"
                        }
                   }';
    
                   // echo $request; 
                   //  exit;

    $msg = Request($request);
    return $msg;
    // echo "Response Data : ".json_encode($msg);


    //     // Hapus2("inacbg_data_terkirim", "no_sep='".$nomor_sep."'");
    //     // InsertData2("inacbg_data_terkirim","'".$nomor_sep."','".$coder_nik."'");
    //     // GroupingStage1($nomor_sep,$coder_nik);
    // }
}

// 
function groupingAwal($nomor_sep, $tgl_masuk, $tgl_pulang, $jenis_rawat, $kelas_rawat, $diagnosa, $procedure, $nama_dokter, $coder_nik, $konsultasi = 0, $lab = 0, $obat = 0, $non_bedah = 0, $bedah = 0, $keperawatan = 0, $radiologi = 0,$upgrade_class_ind = "", $upgrade_class_class = "", $upgrade_class_los = "", $kamar = 0)
{

    $request = '{
                        "metadata": {
                            "method": "set_claim_data",
                            "nomor_sep": "' . $nomor_sep . '"
                        },
                        "data": {
                            "nomor_sep": "' . $nomor_sep . '",
                            "nomor_kartu": "",
                            "tgl_masuk": "' . $tgl_masuk . '",
                            "tgl_pulang": "' . $tgl_pulang . '",
                            "jenis_rawat": "' . $jenis_rawat . '",
                            "kelas_rawat": "' . $kelas_rawat . '",
                            "adl_sub_acute": "",
                            "adl_chronic": "",
                            "icu_indikator": "",
                            "icu_los": "",
                            "ventilator_hour": "",
                            "upgrade_class_ind": "' . $upgrade_class_ind . '",
                            "upgrade_class_class": "' . $upgrade_class_class . '",
                            "upgrade_class_los": "' . $upgrade_class_los . '",
                            "add_payment_pct": "",
                            "birth_weight": "",
                            "discharge_status": "",
                            "diagnosa": "' . $diagnosa . '",
                            "procedure": "' . $procedure . '",
                            "tarif_rs": {
                                "prosedur_non_bedah": "' . $non_bedah . '",
                                "prosedur_bedah": "' . $bedah . '",
                                "konsultasi": "' . $konsultasi . '",
                                "tenaga_ahli": "0",
                                "keperawatan": "' . $keperawatan . '",
                                "penunjang": "0",
                                "radiologi": "' . $radiologi . '",
                                "laboratorium": "' . $lab . '",
                                "pelayanan_darah": "0",
                                "rehabilitasi": "0",
                                "kamar": "' . $kamar . '",
                                "rawat_intensif": "0",
                                "obat": "' . $obat . '",
                                "obat_kronis": "0",
                                "obat_kemoterapi": "0",
                                "alkes": "0",
                                "bmhp": "0",
                                "sewa_alat": "0"
                             },
                            "tarif_poli_eks": "0",
                            "nama_dokter": "' . $nama_dokter . '",
                            "kode_tarif": "' . getKelasRS() . '",
                            "payor_id": "3",
                            "payor_cd": "JKN",
                            "cob_cd": "",
                            "coder_nik": "' . $coder_nik . '"
                        }
                   }';
    
                   // echo $request; 
                   //  exit;
    $msg = Request($request);
    return $msg;
    // echo "Response Data : ".json_encode($msg);


    //     // Hapus2("inacbg_data_terkirim", "no_sep='".$nomor_sep."'");
    //     // InsertData2("inacbg_data_terkirim","'".$nomor_sep."','".$coder_nik."'");
    //     // GroupingStage1($nomor_sep,$coder_nik);
    // }
}

function validateSITB($nomor_sep, $id_sitb)
{
    $request = '{
                        "metadata": {
                            "method":"sitb_validate"
                        },
                        "data": {
                            "nomor_sep":"' . $nomor_sep . '",
                            "nomor_register_sitb":"' . $id_sitb . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}
function GroupingAwalInacbg($nomor_sep, $coder_nik)
{
    $request = '{
                        "metadata": {
                            "method":"grouper",
                            "stage":"1"
                        },
                        "data": {
                            "nomor_sep":"' . $nomor_sep . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}

function CekKlaim($nomor_sep)
{
    $request = '{
                        "metadata": {
                            "method":"get_claim_data"
                        },
                        "data": {
                            "nomor_sep":"' . $nomor_sep . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}
function CekDiagnosa($kode_diagnosa)
{
    $request = '{
                        "metadata": {
                            "method":"search_diagnosis"
                        },
                        "data": {
                            "keyword":"' . $kode_diagnosa . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}
function CekDiagnosaInaGrouper($kode_diagnosa)
{
    $request = '{
                        "metadata": {
                            "method":"search_diagnosis_inagrouper"
                        },
                        "data": {
                            "keyword":"' . $kode_diagnosa . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}
function CekProcedure($kode_procedure)
{
    $request = '{
                        "metadata": {
                            "method":"search_procedures"
                        },
                        "data": {
                            "keyword":"' . $kode_procedure . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}
function CekProcedureInaGrouper($kode_procedure)
{
    $request = '{
                        "metadata": {
                            "method":"search_procedures_inagrouper"
                        },
                        "data": {
                            "keyword":"' . $kode_procedure . '"
                        }
                   }';
    $msg = Request($request);
    return $msg;
}

function UpdateDataPasein($nomor_rm, $nomor_kartu, $nama_pasien, $tgl_lahir, $jk)
{
    switch ($jk) {
        case "L":
            $genderCnv = 1;
            break;
        case "P":
            $genderCnv = 2;
            break;
        default:
            $genderCnv = 1;
    }
    $request = '{
                        "metadata": {
                            "method":"update_patient",
                            "nomor_rm":"' . $nomor_rm . '"
                        },
                        "data": {
                           "nomor_kartu": "' . $nomor_kartu . '",
                            "nomor_rm": "' . $nomor_rm . '",
                            "nama_pasien": "' . $nama_pasien . '",
                            "tgl_lahir": "' . $tgl_lahir . '",
                            "gender": "' . $genderCnv . '"
                        
                        }
                   }';
    $msg = Request($request);
    return $msg;
}
function CetakKlaim($nomor_sep)
{
    $request = '{
                        "metadata": {
                            "method": "claim_print"
                        },
                        "data": {
                            "nomor_sep": "' . $nomor_sep . '"
                        }
                   }';
    $msg = Request($request);
    if ($msg['metadata']['code'] == 200) {
        // var_dump($msg);
        // echo $msg['metadata']['message']."";
        // echo $msg['data'];
        $pdf_base64 = $msg['data'];
        //Get File content from txt file
        // $pdf_base64_handler = $pdf_base64;
        // $pdf_content = fread ($pdf_base64_handler,filesize($pdf_base64));
        // fclose ($pdf_base64_handler);
        //Decode pdf content
        $pdf_decoded = base64_decode($pdf_base64);
        //Write data back to pdf file
        $pdf = fopen('berkasdigital/data_individual/individual_' . $nomor_sep . '.pdf', 'w');
        fwrite($pdf, $pdf_decoded);
        //close output file
        fclose($pdf);
        $response = array(
            'metadata' => array(
                'message' => 'Data Individual Sukses Diimport',
                'code' => 200
            )
        );
        http_response_code(200);
    } else {
        unlink("berkasdigital/data_individual/individual_' . $nomor_sep . '.pdf");
        $response = array(
            'metadata' => array(
                'message' =>  $msg['metadata']['message'] . "",
                'code' => $msg['metadata']['code']
            )
        );
        http_response_code((int)$msg['metadata']['code']);
    }


    echo json_encode($response);
}

function Request($request)
{
    $json = mc_encrypt($request, getKey());
    $header = array("Content-Type: application/x-www-form-urlencoded");
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, getUrlWS());
    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    $response = curl_exec($ch);
    $first = strpos($response, "\n") + 1;
    $last = strrpos($response, "\n") - 1;
    $hasilresponse = substr($response, $first, strlen($response) - $first - $last);
    $hasildecrypt = mc_decrypt($hasilresponse, getKey());
    //echo $hasildecrypt;
    $msg = json_decode($hasildecrypt, true);
    return $msg;
}
