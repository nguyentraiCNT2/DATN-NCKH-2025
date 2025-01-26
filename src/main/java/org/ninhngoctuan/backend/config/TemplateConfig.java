package org.ninhngoctuan.backend.config;

public class TemplateConfig {
    public static final String TEMPALTE_EMAIL_VARIFIED = "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "    <title>Xác thực tài khoản</title>\n" +
                            "    <style>\n" +
                            "        .card {\n" +
                            "            width: 40%;\n" +
                            "            border-radius: 20px;\n" +
                            "            background: #ffffff;\n" +
                            "            padding: 5px;\n" +
                            "            overflow: hidden;\n" +
                            "            box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 20px 0px;\n" +
                            "            transition: transform 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);\n" +
                            "            color: #333333;\n" +
                            "            margin: 0 auto;\n" +
                            "            font-family: Arial, sans-serif;\n" +
                            "            margin-top: 50px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card:hover {\n" +
                            "            transform: scale(1.05);\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .top-section {\n" +
                            "            height: 150px;\n" +
                            "            border-radius: 15px;\n" +
                            "            background: linear-gradient(45deg, rgb(72, 191, 227) 0%, rgb(135, 222, 243) 100%);\n" +
                            "            position: relative;\n" +
                            "            display: flex;\n" +
                            "            flex-direction: column;\n" +
                            "            align-items: center;\n" +
                            "            justify-content: center;\n" +
                            "            padding-top: 15px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .top-section .border {\n" +
                            "            border-bottom-right-radius: 20px;\n" +
                            "            border-top-left-radius: 20px;\n" +
                            "            height: 30px;\n" +
                            "            width: 130px;\n" +
                            "            background: #ffffff;\n" +
                            "            position: relative;\n" +
                            "            font-size: 24px;\n" +
                            "            font-weight: bold;\n" +
                            "            letter-spacing: 2px;\n" +
                            "            padding: 10px 10px;\n" +
                            "            text-align: center;\n" +
                            "            color: #333333;\n" +
                            "            margin-left: 33%;" +
                            "            margin-top: 30px;" +
                            "            box-shadow: -10px -10px 0 0 #ffffff;\n" +
                            "        }\n" +
                            "\n" +
                            "            .otp-code {\n" +
                            "             margin: auto;\n" +
                            "            font-size: 24px;\n" +
                            "            width: 30%;\n" +
                            "            font-weight: bold;\n" +
                            "            background: #f0f4ff;\n" +
                            "            color: #2e7dff;\n" +
                            "            padding: 10px 30px;\n" +
                            "            text-align: center;\n" +
                            "            border-radius: 5px;\n" +
                            "            letter-spacing: 2px;\n" +
                            "            margin-top: 15px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section {\n" +
                            "            margin-top: 15px;\n" +
                            "            padding: 10px 5px;\n" +
                            "            text-align: center;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section .title {\n" +
                            "            font-size: 17px;\n" +
                            "            font-weight: bolder;\n" +
                            "            color: #2e7dff;\n" +
                            "            text-align: center;\n" +
                            "            letter-spacing: 2px;\n" +
                            "            margin-bottom: 15px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section .description {\n" +
                            "            color: #555555;\n" +
                            "            font-size: 14px;\n" +
                            "            margin-bottom: 10px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section .expiry {\n" +
                            "            font-size: 12px;\n" +
                            "            color: #888888;\n" +
                            "        }\n" +
                            "\n" +
                            "        .email-footer {\n" +
                            "            text-align: center;\n" +
                            "            font-size: 10px;\n" +
                            "            color: #aaaaaa;\n" +
                            "            margin-top: 10px;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <div class=\"card\">\n" +
                            "        <div class=\"top-section\">\n" +
                            "            <div class=\"border\">\n" +
                            "                Đại học Nguyễn Trãi\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "        <div class=\"bottom-section\">\n" +
                            "            <span class=\"title\">Xác thực tài khoản</span>\n" +
                            "            <p class=\"description\">\n" +
                            "                Chào bạn, <br> Đây là mã OTP của bạn để xác thực tài khoản:\n" +
                            "            </p>\n" +
                            "            <div class=\"otp-code\">{{otp_code}}</div>\n" +
                            "\n" +
                            "            <p class=\"expiry\">Mã này sẽ hết hạn trong 5 phút. Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>\n" +
                            "        </div>\n" +
                            "        <div class=\"email-footer\">© 2024 Your Company. All rights reserved.</div>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>\n";

    public static final String TEMPLATE_PASSWORD_FOR_GET = "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "    <title>Thay đổi mật khẩu</title>\n" +
                            "    <style>\n" +
                            "        /* Styles for the OTP card */\n" +
                            "        .card {\n" +
                            "            width: 40%;\n" +
                            "            border-radius: 20px;\n" +
                            "            background: #ffffff;\n" +
                            "            padding: 5px;\n" +
                            "            overflow: hidden;\n" +
                            "            box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 20px 0px;\n" +
                            "            transition: transform 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);\n" +
                            "            color: #333333;\n" +
                            "            margin: 0 auto;\n" +
                            "            font-family: Arial, sans-serif;\n" +
                            "            margin-top: 50px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card:hover {\n" +
                            "            transform: scale(1.05);\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .top-section {\n" +
                            "            height: 150px;\n" +
                            "            border-radius: 15px;\n" +
                            "            background: linear-gradient(45deg, rgb(227, 224, 72) 0%, rgb(243, 241, 135) 100%);\n" +
                            "            position: relative;\n" +
                            "            display: flex;\n" +
                            "            flex-direction: column;\n" +
                            "            align-items: center;\n" +
                            "            justify-content: center;\n" +
                            "            padding-top: 15px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .top-section .border {\n" +
                            "            border-bottom-right-radius: 20px;\n" +
                            "            border-top-left-radius: 20px;\n" +
                            "            height: 30px;\n" +
                            "            width: 130px;\n" +
                            "            background: #ffffff;\n" +
                            "            position: relative;\n" +
                            "            font-size: 24px;\n" +
                            "            font-weight: bold;\n" +
                            "            letter-spacing: 2px;\n" +
                            "            padding: 10px 10px;\n" +
                            "            text-align: center;\n" +
                            "            color: #333333;\n" +
                            "            margin-left: 33%;" +
                            "            margin-top: 30px;" +
                            "            box-shadow: -10px -10px 0 0 #ffffff;\n" +
                            "        }\n" +
                            "\n" +
                            "            .otp-code {\n" +
                            "             margin: auto;\n" +
                            "            font-size: 24px;\n" +
                            "            width: 30%;\n" +
                            "            font-weight: bold;\n" +
                            "            background: #f0f4ff;\n" +
                            "            color: #decd32;\n" +
                            "            padding: 10px 30px;\n" +
                            "            text-align: center;\n" +
                            "            border-radius: 5px;\n" +
                            "            letter-spacing: 2px;\n" +
                            "            margin-top: 15px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section {\n" +
                            "            margin-top: 15px;\n" +
                            "            padding: 10px 5px;\n" +
                            "            text-align: center;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section .title {\n" +
                            "            font-size: 17px;\n" +
                            "            font-weight: bolder;\n" +
                            "            text-align: center;\n" +
                            "            letter-spacing: 2px;\n" +
                            "            margin-bottom: 15px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section .description {\n" +
                            "            color: #555555;\n" +
                            "            font-size: 14px;\n" +
                            "            margin-bottom: 10px;\n" +
                            "        }\n" +
                            "\n" +
                            "        .card .bottom-section .expiry {\n" +
                            "            font-size: 12px;\n" +
                            "            color: #888888;\n" +
                            "        }\n" +
                            "\n" +
                            "        .email-footer {\n" +
                            "            text-align: center;\n" +
                            "            font-size: 10px;\n" +
                            "            color: #aaaaaa;\n" +
                            "            margin-top: 10px;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <div class=\"card\">\n" +
                            "        <div class=\"top-section\">\n" +
                            "            <div class=\"border\">\n" +
                            "                Đại học Nguyễn Trãi\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "        <div class=\"bottom-section\">\n" +
                            "            <span class=\"title\">Thay đổi mật khẩu</span>\n" +
                            "            <p class=\"description\">\n" +
                            "                Chào bạn, <br> Đây là mã OTP của bạn để thay đổi mật khẩu:\n" +
                            "            </p>\n" +
                            "            <div class=\"otp-code\">{{otp_code}}</div>\n" +
                            "\n" +
                            "            <p class=\"expiry\">Mã này sẽ hết hạn trong 5 phút. Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>\n" +
                            "        </div>\n" +
                            "        <div class=\"email-footer\">© 2024 Your Company. All rights reserved.</div>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>\n";

}
