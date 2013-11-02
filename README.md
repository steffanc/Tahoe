Tahoe
=====

Tahoe real time marketplace for Android

Setup instructions
------------------

Migrating away from Gradle project:

1. Close your existing project.

2. git clean your current repo

3. git checkout this branch

4. Reimport the project into Android Studio.  Do not choose Gradle/Maven type of build.

   ![image](https://f.cloud.github.com/assets/326857/1445194/eba67782-421a-11e3-98aa-9d25a9e7e11a.png)

5. Setup your Project Structure->Modules->Tahoe as such (i.e. make sure you have Google Play Services + Facebook module added as a Dependency:

   ![image](https://f.cloud.github.com/assets/326857/1445057/abe683ce-4217-11e3-9c66-7e268a4c2598.png)

6. Add the Google Play and Facebook module as a dependency to the Tahoe module (look for the + sign at the right side):

   ![image](https://f.cloud.github.com/assets/326857/1445028/307d4ec0-4217-11e3-8417-181d0db54f69.png)

   You should see under Tahoe Google Play Services + Facebook added and the Export button clicked:

   ![image](https://f.cloud.github.com/assets/326857/1445499/431196bc-4222-11e3-8f80-bbc3bb3afe73.png)

7. Make sure to mark the Google Play Services as a Library module.  Otherwise, you may get NoClassDefError issues for
com.google.android.gms.R$styleable.

   ![image](https://f.cloud.github.com/assets/326857/1460057/438638aa-440c-11e3-8f3b-05e8b21ece58.png)

If you get NoClassDef exceptions for the Google Play or Facebook, make sure you have made sure you followed steps 6 & 7.

   ![image](https://f.cloud.github.com/assets/326857/1445231/b6f6fe84-421b-11e3-99b0-51480a7cf002.png)

Other stuff
-----------
* Verify that you can traverse the libs/ dir (there should be + signs).

  ![image](https://f.cloud.github.com/assets/326857/1445048/777ae8b4-4217-11e3-9ec0-29b0031527ac.png)

If not, Add as Library -- in Linux, I trigger the right click.

* One useful tip -- inside Edit->Run Configurations, make sure to clear LogCat per
run.  It's often easy to get confused of stack traces:

  ![image](https://f.cloud.github.com/assets/326857/1445221/6f620f78-421b-11e3-9708-df6185495289.png)

* Make sure to friend Jon Azoff and become a joint developer on his app.

  You may need to go to https://developers.facebook.com/apps and look for the tiny yellow bar.

  See the part on https://developers.facebook.com/docs/android/getting-started/ and add your hash to the developer app:

  ```
  keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64

  You will be prompted for a password. This should be 'android' without
  quotes. You'll then be given a key hash of 30 characters or so. (If you are not
  prompted for a password, something is wrong and you must check your paths above
  to ensure the debug.keystore is present.)
  ```

* Add your hash also to the Google API console (https://code.google.com/apis/console/b/1/?pli=1#project:261246281766:access)```
