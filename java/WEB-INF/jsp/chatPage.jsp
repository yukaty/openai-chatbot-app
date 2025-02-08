<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Java Tutor App</title>
<link rel="stylesheet"
    href="<%= request.getContextPath() %>/css/style.css">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
    href="https://fonts.googleapis.com/css2?family=NotoSansJP&display=swap"
    rel="stylesheet">
</head>

<body>
    <header>
        <nav>
            <a href="chat">Java Tutor App</a>
        </nav>
    </header>
    <main>
        <article>
            <section>
                <h2>Question</h2>
                <form action="<%= request.getContextPath() %>/chat" method="post">
                    <div>
                        <textarea name="question" cols="60" rows="6" maxlength="300"
                            required>${param.question}</textarea>
                    </div>
                    <div class="submit-btn">
                        <button type="submit" name="submit" value="ask">Ask</button>
                    </div>
                </form>
            </section>
            <section>
                <h2>Answer</h2>
                <p>
                    ${answer != null ? answer : "The answer will be displayed here when you enter the question content and click the \"Ask\" button."}
                </p>
            </section>
        </article>
    </main>
    <footer>
        <p class="copyright">&copy; Java Tutor App</p>
    </footer>
</body>

</html>