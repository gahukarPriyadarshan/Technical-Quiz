package org.priyadarshan.technicalquiz.utility;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.dao.ApiKeyDao;
import org.priyadarshan.technicalquiz.pojo.ApiKey;
import org.priyadarshan.technicalquiz.pojo.UserApiKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Data
@RequiredArgsConstructor
public class KeyAuthenticator {

    private final JoinUsersAndApiKey joinUsersAndApiKey;
    private final ApiKeyDao apiKeyDao;
    private String key;
    Optional<ApiKey> apiKeyObj;

    public boolean isKeyValid(){
        apiKeyObj = apiKeyDao.findByApiKey(key);
        return apiKeyObj.isPresent();
    }

    public boolean isKeyValidAndAdmin(){
        List<UserApiKey> userApiKeys = joinUsersAndApiKey.getEmailRoleAndApiKeyByApiKey();
        return !userApiKeys.isEmpty();
    }

}
