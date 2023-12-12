package com.example.volunteer_campaign_management.controller;

import com.example.volunteer_campaign_management.configs.VNPayConfig;
import com.example.volunteer_campaign_management.entities.ContributionsEntity;
import com.example.volunteer_campaign_management.repositories.ContributionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class VNPayController {
    private static final String SECRET_KEY1 = "donatechotao1234"; // Now it's 16 bytes
    private static final String random = generateRandomKey();

    private static String generateRandomKey() {
        byte[] key = new byte[16]; // 16 bytes for AES-128, change this to 24 or 32 for AES-192 or AES-256
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    @Autowired
    private ContributionsRepository repository;
    private String encrypt(String value) {
        try {
            byte[] keyBytes = SECRET_KEY1.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // Handle encryption exception
            e.printStackTrace();
            return null; // or throw a custom exception if needed
        }
    }



    private String decrypt(String encryptedValue) {
        try {
            byte[] keyBytes = SECRET_KEY1.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Handle decryption exception
            e.printStackTrace();
            return null; // or throw a custom exception if needed
        }
    }


    @GetMapping("/test")
    public String test(@RequestParam("test") String test){
        return decrypt(test);
    }

    @GetMapping("/pay")
    public String getPay(@RequestParam("price") long price,
                         @RequestParam("name") String name,
                         @RequestParam("description") String description) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = price * 100;
        String bankCode = "NCB";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        String encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8.toString());
        String maHoaToken = encrypt(random);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl + "?name=" + encodedName + "&description=" + encodedDescription + "&detoken=" + maHoaToken);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    @GetMapping("/checkPay2")
    public RedirectView checkPaymentStatus(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("description") String encodedDescription,
            @RequestParam("name") String encodedName,
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("detoken") String detoken
    ) {
        try {
            String decryptedToken = decrypt(detoken);
            if (random.equals(decryptedToken)) {

                String decodedName = URLDecoder.decode(encodedName, StandardCharsets.UTF_8.toString());
                String decodedDescription = URLDecoder.decode(encodedDescription, StandardCharsets.UTF_8.toString());

                if ("00".equals(responseCode)) {
                    // Thanh toán thành công
                    Date currentDate = new Date();

                    ContributionsEntity newObj = new ContributionsEntity();
                    newObj.setDonationDay(currentDate);


                    if (decodedName.isEmpty() && decodedDescription.isEmpty()) {
                        newObj.setDescription("User anonymous đã quyên góp cho chiến dịch mùa hè xanh");
                        newObj.setName("User anonymous");
                    } else if (decodedName.isEmpty()) {
                        newObj.setDescription(decodedDescription);
                        newObj.setName("User anonymous");
                    } else if (decodedDescription.isEmpty()) {
                        newObj.setDescription(decodedName + " đã quyên góp cho chiến dịch mùa hè xanh");
                        newObj.setName(decodedName);
                    } else {
                        newObj.setDescription(decodedDescription);
                        newObj.setName(decodedName);
                    }

                    newObj.setPrice(Float.parseFloat(amount));
                    repository.save(newObj);



                    return new RedirectView(VNPayConfig.urlSuccess);
                } else {
                    // Xử lý thanh toán thất bại
                    return new RedirectView(VNPayConfig.urlFail);
                }
            } else {
                // Token không hợp lệ
                return new RedirectView(VNPayConfig.urlFail);
            }
        } catch (UnsupportedEncodingException e) {
            return new RedirectView(VNPayConfig.urlFail);
        }
    }


}
