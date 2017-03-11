This is a biometric identification system which uses
keystroke dynamics to verify the identity of the user.

It measures the average dwell time for each key typed
by the user and in verification, checks if the deviation
for each key is more than 20 milli seconds.

If the deviation is more than 20 milli seconds, it is
assumed that the user failed to enter that key correctly.

If the user fails 10 or more such keys, he is denied
access to the syste.

eg:
If a user dwells on letter 'a' for 90 milli seconds
on average, then any dwell time below 70 ms or above
110 ms will result in the subsequent user failing to 
fit the recognized period and failing that letter.

It is assumed that independent of the condition of
the user, he will be able to type more than 16 letters in
his usual rhythm.