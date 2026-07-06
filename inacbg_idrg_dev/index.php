<?php
require_once('conf/command.php');
require_once('conf/wsinacbg.php');
//  require_once('../conf/conf.php');
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST, GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

$act     = isset($_GET['act']) ? $_GET['act'] : NULL;
$url     = isset($_GET['url']) ? $_GET['url'] : '/';
$noSep     = isset($_GET['noSep']) ? $_GET['noSep'] : NULL;
$nikCoder     = isset($_GET['nikCoder']) ? $_GET['nikCoder'] : NULL;

$header  = apache_request_headers();
$method = $_SERVER['REQUEST_METHOD'];
if ($method == 'GET') {
    if ($act == "cekSep") {
        CetakKlaim($noSep, $nikCoder);
    } else if ($act == "cekDiagnosa") {
        $kodeDiagnosa     = isset($_GET['kodeDiagnosa']) ? $_GET['kodeDiagnosa'] : NULL;
        $result_diagnosa = CekDiagnosa($kodeDiagnosa);
        $length =  count($result_diagnosa['response']['data']);
        if ($length > 0) {
            $dataList = $result_diagnosa['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'kode' => $dataList[$i][1],
                    'description' => $dataList[$i][0],

                ];
            }
        } else {
            $data = "";
        }
        $diagnosa = [
            'metadata' => [
                'code' => $result_diagnosa['metadata']['code'],
                'message' => $result_diagnosa['metadata']['message']
            ],
            'response' => [
                'count' => $result_diagnosa['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($diagnosa, true);
    } else if ($act == "searchDiagnosa") {
        $kodeDiagnosa     = isset($_GET['kodeDiagnosa']) ? $_GET['kodeDiagnosa'] : NULL;
        $result_diagnosa = CekDiagnosa($kodeDiagnosa);
        $length =  count($result_diagnosa['response']['data']);
        if ($length > 0) {
            $dataList = $result_diagnosa['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'kode' => $dataList[$i][1],
                    'description' => $dataList[$i][0],

                ];
            }
        } else {
            $data = "";
        }
        $diagnosa = [
            'metadata' => [
                'code' => $result_diagnosa['metadata']['code'],
                'message' => $result_diagnosa['metadata']['message']
            ],
            'response' => [
                'count' => $result_diagnosa['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($diagnosa, true);
    } else if ($act == "searchDiagnosisInagrouper") {
        $kodeDiagnosa     = isset($_GET['kodeDiagnosa']) ? $_GET['kodeDiagnosa'] : NULL;
        $result_diagnosa = CekDiagnosaInaGrouper($kodeDiagnosa);
        $length =  count($result_diagnosa['response']['data']);
        if ($length > 0) {
            $dataList = $result_diagnosa['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'code' => $dataList[$i]["code"],
                    'description' => $dataList[$i]["description"],
                    'validcode' => $dataList[$i]["validcode"],
                    'accpdx' => $dataList[$i]["accpdx"],
                    'code_asterisk' => $dataList[$i]["code_asterisk"],
                    'asterisk' => $dataList[$i]['asterisk'],
                    'im' => $dataList[$i]["im"]
                ];
            }
        } else {
            $data = "";
        }
        $diagnosa = [
            'metadata' => [
                'code' => $result_diagnosa['metadata']['code'],
                'message' => $result_diagnosa['metadata']['message']
            ],
            'response' => [
                'count' => $result_diagnosa['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($diagnosa, true);
    } else if ($act == "cekProcedure") {
        $kodeProcedure   = isset($_GET['kodeProcedure']) ? $_GET['kodeProcedure'] : NULL;
        $result_procedure = CekProcedure($kodeProcedure);
        $length =  count($result_procedure['response']['data']);
        if ($length > 0) {
            $dataList = $result_procedure['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'kode' => $dataList[$i][1],
                    'description' => $dataList[$i][0]
                ];
            }
        } else {
            $data = "";
        }
        $procedure = [
            'metadata' => [
                'code' => $result_procedure['metadata']['code'],
                'message' => $result_procedure['metadata']['message']
            ],
            'response' => [
                'count' => $result_procedure['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($procedure, true);
    } else if ($act == "searchProcedure") {
        $kodeProcedure   = isset($_GET['kodeProcedure']) ? $_GET['kodeProcedure'] : NULL;
        $result_procedure = CekProcedure($kodeProcedure);
        $length =  count($result_procedure['response']['data']);
        if ($length > 0) {
            $dataList = $result_procedure['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'kode' => $dataList[$i][1],
                    'description' => $dataList[$i][0]
                ];
            }
        } else {
            $data = "";
        }
        $procedure = [
            'metadata' => [
                'code' => $result_procedure['metadata']['code'],
                'message' => $result_procedure['metadata']['message']
            ],
            'response' => [
                'count' => $result_procedure['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($procedure, true);
    } else if ($act == "searchProcedureInagrouper") {
        $kodeProcedure   = isset($_GET['kodeProcedure']) ? $_GET['kodeProcedure'] : NULL;
        $result_procedure = CekProcedureInaGrouper($kodeProcedure);
        $length =  count(($result_procedure['response']['data'] == null) ? 0 : $result_procedure['response']['data']);
        if ($length > 0) {
            $dataList = $result_procedure['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'code' => $dataList[$i]["code"],
                    'description' => $dataList[$i]["description"],
                    'validcode' => $dataList[$i]["validcode"],
                    'im' => $dataList[$i]["im"]
                ];
            }
        } else {
            $data = "";
        }
        $procedure = [
            'metadata' => [
                'code' => $result_procedure['metadata']['code'],
                'message' => $result_procedure['metadata']['message']
            ],
            'response' => [
                'count' => $result_procedure['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($procedure, true);
    } else if ($act == "CekProcedureInagrouper") {
        $kodeProcedure   = isset($_GET['kodeProcedure']) ? $_GET['kodeProcedure'] : NULL;
        $result_procedure = CekProcedureInaGrouper($kodeProcedure);
        $length =  count($result_procedure['response']['data']);
        if ($length > 0) {
            $dataList = $result_procedure['response']['data'];
            for ($i = 0; $i < $length; $i++) {
                $data[] = [
                    'code' => $dataList[$i]["code"],
                    'description' => $dataList[$i]["description"],
                    'validcode' => $dataList[$i]["validcode"],
                    'im' => $dataList[$i]["im"]
                ];
            }
        } else {
            $data = "";
        }
        $procedure = [
            'metadata' => [
                'code' => $result_procedure['metadata']['code'],
                'message' => $result_procedure['metadata']['message']
            ],
            'response' => [
                'count' => $result_procedure['response']['count'],
                'data' =>
                $data

            ]
        ];
        echo json_encode($procedure, true);
    }
} else   if ($method == 'POST') {
    $konten = trim(file_get_contents("php://input"));
    $decode = json_decode($konten, true);


    if ($act == "createClaim") {
        $claim = CreateClaim($decode['no_jkn'], $decode['no_sep'], $decode['no_rm'], $decode['nama_pasien'], $decode['tgl_lahir'], $decode['jk'], $decode['no_reg']);
        if ($claim['metadata']['message'] == "Ok") {
            $group = setClaim($decode['no_sep'], $decode['no_jkn'], $decode['tgl_awal'], $decode['tgl_akhir'], $decode['cara_masuk'], $decode['jenis'], $decode['kelas_rawat'], $decode['sistole'], $decode['diastole'], $decode['dokter'], $decode['adl_sub_acute'], $decode['adl_chronic'], $decode['icu_indikator'], $decode['icu_los'], $decode['use_ind'], $decode['start_dttm'], $decode['stop_dttm'], $nikCoder, $decode['birth_weight'], $decode['konsultasi'], $decode['lab'], $decode['obat'], $decode['non_bedah'], $decode['bedah'], $decode['keperawatan'], $decode['radiologi'], $decode['kamar'], $decode['tenaga_ahli'], $decode['penunjang'], $decode['pelayanan_darah'], $decode['rehabilitasi'], $decode['rawat_intensif'], $decode['obat_kronis'], $decode['alkes'], $decode['bmhp'], $decode['sewa_alat'], $decode['upgrade_class_ind'], $decode['upgrade_class_class'], $decode['upgrade_class_los'], $decode['tarif_poli_eks']);


            echo json_encode($group);
        } else {
            echo json_encode($claim);
        }

        // echo json_encode( CreateClaim($decode['no_jkn'],$decode['no_sep'] ,$decode['no_rm'],$decode['nama_pasien'],$decode['tgl_lahir'],$decode['jk'],$decode['no_reg']));
    } else if ($act == "updateClaim") {
        $group = setClaim($decode['no_sep'], $decode['no_jkn'], $decode['tgl_awal'], $decode['tgl_akhir'], $decode['cara_masuk'], $decode['jenis'], $decode['kelas_rawat'], $decode['sistole'], $decode['diastole'], $decode['dokter'], $decode['adl_sub_acute'], $decode['adl_chronic'], $decode['icu_indikator'], $decode['icu_los'], $decode['use_ind'], $decode['start_dttm'], $decode['stop_dttm'], $nikCoder, $decode['birth_weight'], $decode['konsultasi'], $decode['lab'], $decode['obat'], $decode['non_bedah'], $decode['bedah'], $decode['keperawatan'], $decode['radiologi'], $decode['kamar'], $decode['tenaga_ahli'], $decode['penunjang'], $decode['pelayanan_darah'], $decode['rehabilitasi'], $decode['rawat_intensif'], $decode['obat_kronis'], $decode['alkes'], $decode['bmhp'], $decode['sewa_alat'], $decode['upgrade_class_ind'], $decode['upgrade_class_class'], $decode['upgrade_class_los'], $decode['tarif_poli_eks']);
        echo json_encode($group);
    } else if ($act == "validateSITB") {
        $group = validateSITB($decode['no_sep'], $decode['no_reg_sitb']);
        echo json_encode($group);
    } else if ($act == "setDiagnosaIDRG") {
        $response = setDiagnosaIDRG($decode['no_sep'], $decode['diagnosa']);
        $response = setProcedureIDRG($decode['no_sep'], $decode['prosedure']);
        $response = setGrouperIDRG($decode['no_sep']);
        echo json_encode($response);
    } else if ($act == "finalIDRG") {
        $group = finalGrouperIDRG($decode['no_sep']);

        echo json_encode($group);
    } else if ($act == "editIDRG") {
        $group = editGrouperIDRG($decode['no_sep']);

        echo json_encode($group);
    } else if ($act == "importCoding") {
        $group = importCoding($decode['no_sep']);

        echo json_encode($group);
    } else if ($act == "setDiagnosaINACBG") {
        setDiagnosaINACBG($decode['no_sep'], $decode['diagnosa']);
        setProcedureINACBG($decode['no_sep'], $decode['prosedure']);
        $response = setGrouperINACBGStage1($decode['no_sep']);
        echo json_encode($response);
    } else if ($act == "getCmg") {
        $group = getCmg($decode['no_sep'], $decode['speciapCmg']);
        echo json_encode($group);
    } else if ($act == "finalINACBG") {
        $group = setFinalINACBG($decode['no_sep']);
        echo json_encode($group);
    } else if ($act == "editINACBG") {
        $group = setEditINACBG($decode['no_sep']);
        echo json_encode($group);
    } else if ($act == "finalKlaim") {
        $group = setFinalClaim($decode['no_sep'], $nikCoder);
        echo json_encode($group);
    } else if ($act == "editKlaim") {
        $group = editClaim($decode['no_sep']);
        echo json_encode($group);
    } else if ($act == "kirimOnlineKlaim") {
        $group = sendClaim($decode['no_sep']);
        echo json_encode($group);
    } else if ($act == "cetakKlaim") {
        $group = CetakKlaim($decode['no_sep'], $nikCoder);
        echo json_encode($group);
    } else if ($act == "getKlaim") {
        $group = getClaim($decode['no_sep']);
        echo json_encode($group);
    } else if ($act == "groupingClaim") {
        $claim = setDataClaim($decode['no_sep'], $decode['tgl_awal'], $decode['tgl_akhir'], $decode['jenis'], $decode['kelas_rawat'], $decode['adl_sub_acute'], $decode['adl_chronic'], $decode['icu_indikator'], $decode['icu_los'], $decode['use_ind'], $decode['start_dttm'], $decode['stop_dttm'], $decode['diagnosa'], $decode['prosedur'], $decode['dokter'], $nikCoder, $decode['konsultasi'], $decode['lab'], $decode['obat'], $decode['non_bedah'], $decode['bedah'], $decode['keperawatan'], $decode['radiologi'], $decode['kamar'], $decode['upgrade_class_ind'], $decode['upgrade_class_class'], $decode['upgrade_class_los']);
        if ($claim['metadata']['message'] == "Ok") {
            echo json_encode(GroupingAwalInacbg($decode['no_sep'], $nikCoder));
        }
    } else if ($act == "createNgroupingClaim") {
        $claim = CreateClaim($decode['no_jkn'], $decode['no_sep'], $decode['no_rm'], $decode['nama_pasien'], $decode['tgl_lahir'], $decode['jk'], $decode['no_reg']);
        if ($claim['metadata']['message'] == "Ok") {
            $group = setDataClaim($decode['no_sep'], $decode['tgl_awal'], $decode['tgl_akhir'], $decode['jenis'], $decode['kelas_rawat'], $decode['adl_sub_acute'], $decode['adl_chronic'], $decode['icu_indikator'], $decode['icu_los'], $decode['use_ind'], $decode['start_dttm'], $decode['stop_dttm'], $decode['diagnosa'], $decode['prosedur'], $decode['dokter'], $nikCoder, $decode['konsultasi'], $decode['lab'], $decode['obat'], $decode['non_bedah'], $decode['bedah'], $decode['keperawatan'], $decode['radiologi'], $decode['kamar'], $decode['upgrade_class_ind'], $decode['upgrade_class_class'], $decode['upgrade_class_los']);
            if ($group['metadata']['message'] == "Ok") {
                echo json_encode(GroupingAwalInacbg($decode['no_sep'], $nikCoder));
            }
        } else {
            echo json_encode($claim);
        }
    } else if ($act == "UpdateDataPasien") {
        echo json_encode(UpdateDataPasein($decode['no_rm'], $decode['no_jkn'], $decode['nama_pasien'], $decode['tgl_lahir'], $decode['jk']));
    }
} else {
    $response = array(
        'metadata' => array(
            'message' => 'Methode tersebut tidak tersedia',
            'code' => 404
        )
    );
    http_response_code(404);
}
