<?php
require_once 'config.php';
require_once 'ChatService.php';

$question = '';
$answer = '';
$error = '';

// Process form submission
if (isset($_POST['submit'])) {
    $question = trim($_POST['question']);

    if (!empty($question)) {
        try {
            $chatService = new ChatService();
            $answer = $chatService->getResponse($question);
        } catch (Exception $e) {
            $error = $e->getMessage();
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Learn PHP with AI-powered explanations and examples">
    <title>PHP Learning Assistant</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=NotoSans&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <nav>
            <a href="index.php">PHP Learning Assistant</a>
        </nav>
    </header>

    <main>
        <article>
            <section>
                <h1>Ask Your PHP Question</h1>
                <form action="index.php" method="post">
                    <div>
                        <label for="question">Your Question:</label>
                        <textarea
                            name="question"
                            cols="60"
                            rows="6"
                            maxlength="300"
                            required
                            placeholder="Type your PHP question here...">
                            <?= htmlspecialchars($question, ENT_QUOTES, 'UTF-8') ?>
                        </textarea>
                    </div>
                    <div class="submit-btn">
                        <button type="submit" name="submit" value="ask">Ask</button>
                    </div>
                </form>
            </section>

            <section>
                <h2>Explanation</h2>
                <div>
                    <?php if ($error): ?>
                        <div>
                            <?= htmlspecialchars($error, ENT_QUOTES, 'UTF-8') ?>
                        </div>
                    <?php elseif ($answer): ?>
                        <div>
                            <?= nl2br(htmlspecialchars($answer, ENT_QUOTES, 'UTF-8')) ?>
                        </div>
                    <?php else: ?>
                        <div>
                            Ask any PHP-related question and get a clear, beginner-friendly explanation.
                        </div>
                    <?php endif; ?>
                </div>
            </section>
        </article>
    </main>

    <footer>
        <p class="copyright">&copy; <?= date('Y') ?> PHP Learning Assistant </p>
    </footer>
</body>

</html>