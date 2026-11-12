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

$wins = $conn->query("SELECT COUNT(*) as count FROM game_records WHERE game_mode = 'bot' AND result = 'win'")->fetch_assoc()['count'];
$losses = $conn->query("SELECT COUNT(*) as count FROM game_records WHERE game_mode = 'bot' AND result = 'lose'")->fetch_assoc()['count'];
$ties = $conn->query("SELECT COUNT(*) as count FROM game_records WHERE game_mode = 'bot' AND result = 'tie'")->fetch_assoc()['count'];

echo json_encode([
    'wins' => $wins,
    'losses' => $losses,
    'ties' => $ties
]);

$conn->close();
?>
