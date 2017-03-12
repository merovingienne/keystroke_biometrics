# keystroke_biometrics
Biometric identification using keystroke dynamics in Java

# What is this?
This is a biometric identification system which uses keystroke dynamics to verify the identity of the user.

Right now, it only measures the average dwell time for each key typed by the user and in verification, checks if the deviation for each key is more than 20 milli seconds.

If the deviation is more than 20 milli seconds, it is assumed that the user failed to enter that key correctly.

If the user fails 10 or more such keys, he is denied access to the syste.

eg:
If a user dwells on letter 'a' for 90 milli seconds on average, then any dwell time below 70 ms or above 110 ms will result in the subsequent user failing to fit the recognized period and failing that letter.

It is assumed that independent of the condition of the user, he will be able to type more than 16 letters in his usual rhythm. In other words, more than 10 failed letters will deny access to the system.

# Contributions

The code works but is currently a mess with the main class containing all the logic & UI elements. Code refactors and extensions to the feature set are most welcome.

An article about this app can be found at <a href="http://blog.chanukawijayakoon.me/2017/03/biometric-identification-with-keystroke-dynamics-using-java/">my blog.</a>
