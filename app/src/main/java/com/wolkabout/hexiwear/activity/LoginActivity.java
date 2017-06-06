/**
 * Hexiwear application is used to pair with Hexiwear BLE devices
 * and send sensor readings to WolkSense sensor data cloud
 * <p>
 * Copyright (C) 2016 WolkAbout Technology s.r.o.
 * <p>
 * Hexiwear is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Hexiwear is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolkabout.hexiwear.activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wolkabout.hexiwear.R;
import com.wolkabout.wolkrestandroid.Credentials_;
import com.wolkabout.wolkrestandroid.dto.AuthenticationResponseDto;
import com.wolkabout.wolkrestandroid.dto.SignInDto;
import com.wolkabout.wolkrestandroid.service.AuthenticationService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @Pref
    Credentials_ credentials;

    @RestService
    AuthenticationService authenticationService;

    public void onCreate(){
        signIn();
    }

    @Background
    void signIn() {
        try {
            final String emailAddress = "elbert1212@gmail.com";
            final String password = "thisisthegrouppassword";

            final AuthenticationResponseDto response = authenticationService.signIn(new SignInDto(emailAddress, password));
            credentials.username().put(response.getEmail());
            credentials.accessToken().put(response.getAccessToken());
            credentials.refreshToken().put(response.getRefreshToken());
            credentials.accessTokenExpires().put(response.getAccessTokenExpires().getTime());
            credentials.refreshTokenExpires().put(response.getRefreshTokenExpires().getTime());
            MainActivity_.intent(LoginActivity.this).start();
            finish();
        } catch (HttpStatusCodeException e) {
            Log.e(TAG, "signIn: ", e);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return;
            }

        } catch (Exception e) {
            Log.e(TAG, "signIn: ", e);
        }
    }
}
