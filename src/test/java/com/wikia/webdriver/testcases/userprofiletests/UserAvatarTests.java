package com.wikia.webdriver.testcases.userprofiletests;

import com.wikia.webdriver.common.contentpatterns.PageContent;
import com.wikia.webdriver.common.contentpatterns.URLsContent;
import com.wikia.webdriver.common.properties.Credentials;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.componentobject.editprofile.AvatarComponentObject;
import com.wikia.webdriver.pageobjectsfactory.componentobject.notifications.NotificationsComponentObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.signup.UserProfilePageObject;

import org.testng.annotations.Test;

/**
 * @author Karol 'kkarolk' Kujawiak
 * 
 * Test 01
 * 1. Open user profile page User:Username and add avatar
 * 2. Verify that avatar appeared on user  page, and on global navigation
 * 
 * Test 02
 * 1. Open wikia page and click user avatar on global navigation
 * 2. Make sure you were redirected to User page
 * 
 * Test 03
 * 1. Open user profile page User:Username and remove avatar
 * 2. Verify that avatar was removed from user page, and placeholder appeared on global navigation
 * 3. Log out and verify that avatar is not visible on global navigation
 * 
 */
public class UserAvatarTests extends NewTestTemplate {

  Credentials credentials = config.getCredentials();

  @Test(
      groups = {"AvatarTest", "AvatarTest_001"}
  )
  public void AvatarTest_001_uploadAvatar() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    base.logInCookie(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    UserProfilePageObject profile = base.openProfilePage(credentials.userNameStaff, wikiURL);
    AvatarComponentObject avatar = profile.clickEditAvatar();
    avatar.uploadAvatar(PageContent.FILE);
    avatar.saveProfile();
    profile.verifyAvatar(credentials.userNameStaffId);
    profile.verifyAvatarVisible();
    String avatarURL = profile.getAvatarUrl();
    profile.verifyURLStatus(200, avatarURL);
  }

  @Test(
	  groups = {"AvatarTest", "AvatarTest_002"},
	  dependsOnMethods = "AvatarTest_001_uploadAvatar",
	  invocationCount = 10
  )
  public void AvatarTest_002_clickAvatar() {
	  WikiBasePageObject base = new WikiBasePageObject(driver);
	  base.logInCookie(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
	  UserProfilePageObject profile = base.clickOnAvatar();
	  profile.verifyProfilePage(credentials.userNameStaff);
  }
  
  @Test(
      groups = {"AvatarTest", "AvatarTest_003"},
      dependsOnMethods = "AvatarTest_001_uploadAvatar",
      invocationCount = 10
  )
  public void AvatarTest_003_removeAvatar() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    base.logInCookie(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    UserProfilePageObject profile = base.openProfilePage(credentials.userNameStaff, wikiURL);
    profile.clickRemoveAvatar();
    profile.verifyAvatar(URLsContent.AVATAR_GENERIC);
    profile.verifyAvatarPlaceholder();
    profile.logOut(driver);
    profile.verifyAvatarNotPresent();
  }
}
