import os
import json
import requests
from dotenv import load_dotenv
from flask import Flask, request, render_template

# Load environment variables from the .env file
load_dotenv()

# Create a Flask app
app = Flask(__name__)


# Define the index route
@app.route("/", methods=["GET", "POST"])
def index():
    # OpenAI API key
    api_key = os.getenv("OPENAI_API_KEY")

    # OpenAI API endpoint URL
    url = "https://api.openai.com/v1/chat/completions"

    # The answer generated by the OpenAI API
    answer = None
    # Error message for API errors
    error_message = None
    # Form data for the question
    old_question = ""

    # Check if the request method is POST
    if request.method == "POST":
        # Get the question from the form data
        user_question = request.form.get("question", "")
        old_question = user_question

        # HTTP POST request headers
        headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {api_key}",
        }

        # HTTP POST request data
        data = {
            "model": "gpt-4o",
            "messages": [
                {
                    "role": "system",
                    "content": "You are a friendly Python programming tutor.",
                },
                {"role": "user", "content": user_question},
            ],
            "max_tokens": 500,
        }

        try:
            # Request to OpenAI API
            response = requests.post(url, headers=headers, data=json.dumps(data))
            # Check if the response is successful
            response.raise_for_status()
        except requests.exceptions.RequestException as e:
            # If an exception occurs, return an error message
            error_message = f"HTTP request error: {str(e)}"
        else:
            # If the response is successful, parse the JSON response
            result = response.json()
            if "error" in result:
                # If the response contains an error message, return the error message
                api_error_message = result["error"].get("message", "Unknown error")
                error_message = f"API error: {api_error_message}"
            else:
                content = (
                    result.get("choices", [{}])[0].get("message", {}).get("content")
                )
                if content:
                    # Return the content generated by the OpenAI API
                    answer = content
                else:
                    # If the response does not contain the content, return an error message
                    error_message = f"Error: {json.dumps(result, ensure_ascii=False)}"

    # Render the index.html template with the answer and error message
    return render_template(
        "index.html",
        answer=answer,
        error_message=error_message,
        old_question=old_question,
    )


# Run the app when the program starts by running the command `python app.py`
if __name__ == "__main__":
    app.run(debug=True)
