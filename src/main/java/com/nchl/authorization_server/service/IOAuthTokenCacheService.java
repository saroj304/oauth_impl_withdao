package com.nchl.authorization_server.service;

import com.nchl.authorization_server.model.OAuthToken;

public interface IOAuthTokenCacheService {
     void storeToken(String userId, OAuthToken token);
     OAuthToken getToken(String userId);
     void deleteToken(String userId);
}
