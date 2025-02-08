package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChatServlet extends HttpServlet {
    // URLアクセス時に実行されるメソッド
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // チャットアプリのWebページを表示
        request.getRequestDispatcher("/WEB-INF/jsp/chatPage.jsp").forward(request, response);
    }

    // 質問メッセージの送信時に実行されるメソッド
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // リクエストの設定
        request.setCharacterEncoding("UTF-8");

        // ユーザーからの質問メッセージを取得
        String userQuestion = request.getParameter("question");

        // 質問が空でないかチェック
        if (userQuestion == null || userQuestion.trim().isEmpty()) {
            request.setAttribute("answer", "質問は必須です。");
            request.getRequestDispatcher("/WEB-INF/jsp/chatPage.jsp").forward(request, response);
            return;
        }

        // 入力データにエスケープ処理を施す（XSS対策）
        userQuestion = userQuestion.replace("&", "&amp;")
                                   .replace("<", "&lt;")
                                   .replace(">", "&gt;")
                                   .replace("\"", "&quot;")
                                   .replace("'", "&#39;");

        // OpenAIのAPIキー（環境変数から取得）
        String apiKey = System.getenv("OPENAI_API_KEY");
        
        if (apiKey == null) {
            System.err.println("環境変数 OPENAI_API_KEY が設定されていません");
            request.setAttribute("answer", "サーバーの設定に問題があります。管理者に連絡してください。");
        } else {
            System.out.println("APIキー取得成功: " + apiKey.substring(0, 5) + "****"); // 一部だけ表示
        }

        // OpenAI APIのエンドポイント
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // JSONデータを構築
        ObjectMapper mapper = new ObjectMapper();

        // 基本設定
        ObjectNode requestData = mapper.createObjectNode();
        requestData.put("model", "gpt-4o"); // 使用するAIモデル名
        requestData.put("max_tokens", 500); // レスポンスの最大トークン数

        // APIに送信するメッセージ（配列化）
        ArrayNode messages = mapper.createArrayNode()
                .add(mapper.createObjectNode().put("role", "system").put("content", "You are a friendly Java programming tutor."))
                .add(mapper.createObjectNode().put("role", "user").put("content", userQuestion));
        requestData.set("messages", messages);

        // HTTPリクエスト送信用のオブジェクト
        HttpURLConnection connection = null;

        try {
            // APIのエンドポイントURLをURIとして生成し、URLオブジェクトに変換
            URL url = new URI(apiUrl).toURL();

            // エンドポイントとのHTTP接続を確立
            connection = (HttpURLConnection) url.openConnection();

            // HTTPリクエストの設定
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            // JSONデータを送信
            try (OutputStream os = connection.getOutputStream()) {
                mapper.writeValue(os, requestData);
            }

            // ステータスコードを取得
            int statusCode = connection.getResponseCode();

            // ステータスコードに応じたストリームを取得
            try (InputStream inputStream = (statusCode == HttpURLConnection.HTTP_OK) ? connection.getInputStream()
                    : connection.getErrorStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {

                // JSON形式のレスポンスをJavaオブジェクトに変換
                JsonNode jsonResponse = mapper.readTree(br);

                if (statusCode == HttpURLConnection.HTTP_OK) { // 成功時の処理
                    // レスポンスからcontent（回答の本文）を取得
                    String content = jsonResponse
                            .get("choices")
                            .get(0)
                            .get("message")
                            .get("content")
                            .asText();

                    // 受け取った回答をJSPに受け渡す
                    request.setAttribute("answer", content);
                } else { // エラー時の処理
                    // エラーメッセージをJSPに受け渡す
                    request.setAttribute("answer", "レスポンスの受信に失敗しました。ネットワークの状態を確認してください。");
                }
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("例外発生：" + e.getMessage());
            request.setAttribute("answer", "予期しないエラーが発生しました。再試行してください。");
        } finally {
            // HTTP接続を切断
            if (connection != null) {
                connection.disconnect();
            }

            // チャットアプリのWebページを再表示
             request.getRequestDispatcher("/WEB-INF/jsp/chatPage.jsp").forward(request, response);
        }
    }
}
