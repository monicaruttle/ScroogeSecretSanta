# ScroogeSecretSanta
Create a secret santa network, where no user can get paired with someone in the same year or program.

The program reads in a csv file with each row containing the student's name ("Name"), the student's year ("Year"), the student's 4 digit program identifier ("Stream"), the student's email ("Email"), the student's allergies ("Allergies") and a note provided by the student ("Notes"). The columns are in no particular order, however the first row of the csv must have the headers specified in the correct order. For example, the following string would be the first two lines of the input file:

"Name, Year, Stream, Email, Allergies, Notes \n
John Smith, 4, MECH, jsmith@example.com, Peanuts, No candy"


The program outputs a csv file with the first and second column the "Santa's" name and email, and the third, fourth and fifth columns are who the "Santa" is giving the gift to (the "Scrooge"), their allergies and the notes the "Scrooge" provided. Each pairing of students cannot be in the same year or program.
