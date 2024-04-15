package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JWTutils {

	private static final String SECRET_KEY = "ccoK4iHM^qBHpwwvG^";

	public static String generateJwt(String username) throws UnsupportedEncodingException {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		String token = JWT.create()
				.withIssuer("icc352")
				.withSubject("proyecto")
				.withClaim("username", username)
				.sign(algorithm);
		return token;
	}

	public static DecodedJWT decodeJWT(String jwt) throws UnsupportedEncodingException {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		DecodedJWT decodedJWT = JWT.require(algorithm)
				.withIssuer("icc352")
				.build()
				.verify(jwt);
		return decodedJWT;
	}
}
