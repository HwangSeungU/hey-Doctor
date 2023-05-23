package com.heydoctor.app.service.loginpage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.heydoctor.app.dao.UserDAO;
import com.heydoctor.app.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class KakaoService {

    private final UserDAO userDAO;

    @Autowired
    public KakaoService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String getKaKaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=76a60ca6aa2dea1ac80cdec7ec6060b5");
            sb.append("&redirect_uri=http://localhost:10000/login/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body: " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token: " + access_Token);
            log.info("refresh_token: " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public void getKakaoInfo(String token) throws Exception {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body: " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            UserVO user = new UserVO();
            user.setUserName(nickname);
            user.setUserEmail(email);
            user.setUserLoginType("KAKAO");

            saveUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(UserVO userVO) {
        userDAO.save(userVO);
    }

    public void logoutKakao(String token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + token);
            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);

            if (responseCode == 400)
                throw new RuntimeException("An error occurred during Kakao logout");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String br_line;
            String result = "";
            while ((br_line = br.readLine()) != null) {
                result += br_line;
            }
            log.info("result: ");
            log.info(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
