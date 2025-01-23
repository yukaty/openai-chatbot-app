<?php
require 'vendor/autoload.php';

// Load environment variables
$dotenv = Dotenv\Dotenv::createImmutable(__DIR__);
$dotenv->load();

// Configuration constants
define('API_URL', 'https://api.openai.com/v1/chat/completions');
define('API_MODEL', 'gpt-4o');
define('MAX_TOKENS', 500);

// System prompt for the AI tutor
define('SYSTEM_PROMPT', <<<'EOD'
    You are a PHP programming tutor.
    Provide clear, beginner-friendly explanations about PHP concepts, syntax, and best practices.
    Follow these guidelines:
    1. Use simple language and avoid jargon when possible
    2. Always include code examples to illustrate concepts
    3. Explain potential errors or common mistakes related to the topic
    4. When relevant, mention security best practices
    5. Break down complex topics into smaller, manageable parts
EOD);
