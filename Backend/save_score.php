<?php
ini_set('display_errors', 0); // Disable error display in output
error_reporting(0); // Suppress all warnings and notices
header('Content-Type: application/json');
$host = "localhost";
$user = "root";
$pass = "";
$db = "jumblejungle";

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    echo json_encode(['error' => 'Database connection failed']);
    exit;
}

$difficulty = $_POST['difficulty'] ?? '';
$time = $_POST['time'] ?? 0;
$score = $_POST['score'] ?? 0;
$game_mode = $_POST['game_mode'] ?? '';
$result = $_POST['result'] ?? '';

$stmt = $conn->prepare("INSERT INTO game_records (difficulty, time_taken, score, game_mode, result) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("siiss", $difficulty, $time, $score, $game_mode, $result);

if ($stmt->execute()) {
    echo json_encode(['success' => true]);
} else {
    echo json_encode(['error' => 'Insert failed']);
}

$stmt->close();
$conn->close();
?>
