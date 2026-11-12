<?php
header('Content-Type: application/json');
$mysqli = new mysqli("localhost", "root", "", "jumblejungle");

if ($mysqli->connect_error) {
    die(json_encode(["error" => "DB connection failed"]));
}

$difficulties = ['easy', 'medium', 'hard'];
$response = [];

foreach ($difficulties as $diff) {
    $query = "SELECT MAX(score) AS best_score, MIN(time_taken) AS best_time FROM game_records WHERE difficulty = ?";
    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("s", $diff);
    $stmt->execute();
    $result = $stmt->get_result()->fetch_assoc();

    $response[$diff] = [
        "best_score" => (int) $result['best_score'],
        "best_time" => (int) $result['best_time']
    ];
}

echo json_encode($response);
?>
