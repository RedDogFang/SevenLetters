"# SevenLetters" 
(After cloning the repository do your work in a branch (and leave it there). This way we can all work on our pet solution simultaneously.)

Here is the 7 letters challenge.

Given a file of 58K English words find the 7 letters that spell the most words.

Here is an example:
if your 7 letters were abcdefg, then they would spell the words 'cad' and 'beg'. They would also spell 'add' and 'dad'. In other words, duplicate letters in the English words only count as one of the 7 letters.

Once you get the right answer the challenge is just beginning. The real challenge is to do it as quickly as possible. So after getting your code to work you will want to speed it up. 

You cannot take advantage of the fact that the words are English (do not weigh vowels higher because they are vowels). However, other shortcuts are OK. Your code should work if the English words were replaced with random letters.

Another way to look at is that you will need to verify or prove that your answer is correct by exhaustively testing all combos or all combos that cannot be ruled out by other means.

-----------------------------------------
There are two other text files that can be used in addition to englishwords.txt. They are bible.txt and shakespeare.txt. The last two files have multiple words per line. So they will need additional parsing if you want to test against them.

Here are the rules for parsing the text files
1) the code is not case sensitive i.e. 'A' == 'a'
2) treat any characters whose value is less than or equal to a space (' ') as a word seperator i.e. 'bad    day' is two words 'bad' and 'day'
3) all words have at least one character i.e. sequential spaces do not generate a blank word
4) ignore all non letters i.e. 'self-starter' is 'selfstarter'

The main.java supports the following features:
1) easy to average the time over multiple runs
2) results immediately checked for accuracy and time
3) easy to run with different number of letters and different text files
