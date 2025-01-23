<?php
class ChatService {
    private $api_key;
    private $headers;

    // Initialize service with API credentials
    public function __construct() {
        $this->api_key = $_ENV['API_KEY'];
        $this->headers = [
            'Content-Type: application/json',
            'Authorization: Bearer ' . $this->api_key
        ];
    }

    // Get AI response for user question
    public function getResponse($question) {
        $data = [
            'model' => API_MODEL,
            'messages' => [
                ['role' => 'system', 'content' => SYSTEM_PROMPT],
                ['role' => 'user', 'content' => $question]
            ],
            'max_tokens' => MAX_TOKENS
        ];

        return $this->makeApiRequest($data);
    }

    // Send request to OpenAI API
    private function makeApiRequest($data) {
        $curl = curl_init();

        // Setup request parameters
        curl_setopt_array($curl, [
            CURLOPT_URL => API_URL,
            CURLOPT_HTTPHEADER => $this->headers,
            CURLOPT_POST => true,
            CURLOPT_POSTFIELDS => json_encode($data),
            CURLOPT_RETURNTRANSFER => true
        ]);

        try {
            $response = curl_exec($curl);

            // Check for cURL errors
            if (curl_errno($curl)) {
                throw new Exception('cURL Error: ' . curl_error($curl));
            }

            $result = json_decode($response, true);

            // Process API response
            if (isset($result['choices'][0]['message']['content'])) {
                return $result['choices'][0]['message']['content'];
            } elseif (isset($result['error']['message'])) {
                throw new Exception('API Error: ' . $result['error']['message']);
            } else {
                throw new Exception('Unexpected Error: ' . $response);
            }
        } finally {
            curl_close($curl);
        }
    }
}
?>