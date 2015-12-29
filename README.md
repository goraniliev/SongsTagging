# SongsTagging
The goal of this project is to provide Java programs which may predict the popularity of a new song based on its tags (tags may be genres or words which describe the lyrics).  
  
To reproduce the process of prediction, one should have instaleld MySQL, JRE and JDK. The libraries used are attached into jars folder and if there are soem path problems they should be accordingly added to the path of the project (Right click of the project -> Properties -> Build Path -> Add External Library -> add the desired library).  
  
Firstly we populate MySQL Database with data about songs with known number of plays and listeners. This task was challenging and after some improvements on the crawling and using bulk insertons to speed up the insertions, it was still going slow (took more tan 8 hours to insert all songs). Because of the slow insertion process we provide 2 ways to test the code:  
  
First way (if you want to skip the data crawling and insertion step, do the following steps):  
1. Create new empty MySQL Database (let's call it songstagging).  
2.Download dump with data from this link: https://www.dropbox.com/s/lgix4j66aru0683/final_dump.zip?dl=0. Extract it and rename it to be songstaggingdump.sql. Now move it to the parent folder of the database created in 1.  
3. Using the Command Line go to the folder where is located your new empty database (cd /your/path on Linux).  
4. Run this command in the Command Line (with your database credentials in the corresponding fields):  
    mysql -u yourusername --password=yourpass songstagging < songstaggingdump.sql  
5. Check with: select count(*) from song; if the import has been successful. If it succeeded there should be 261357 songs.  
6. Now import the Java project in Eclipse (it was built and tested with Eclipse so we are not sure if it would work easily in different IDE).  
7. To evaluate the prediction start the CrossValidation.java file located in validation package. The running took 25 minutes on i7 CPU @ 2.0 GHz, running on Ubuntu 14.04. The Relative Mean Error of the predictions printed by the program is 0.478. This is only slightly better than random prediction.  
  
Second way (if you want to build the database on your own - it will take more than 8 hours probably):  
1. Create new empty MySQL Database (let's call it songstagging).  
2. Download dump from https://www.dropbox.com/s/8jegttdx9ngyuq8/schema.zip?dl=0. Extract it and rename it to be songstaggingdump.sql. Now move it to the parent folder of the database created in 1.  
3. Using the Command Line go to the folder where is located your new empty database (cd /your/path on Linux).  
4. Run this command in the Command Line (with your database credentials in the corresponding fields):  
    mysql -u yourusername --password=yourpass songstagging < songstaggingdump.sql  
5. Check with: select count(*) from song; if the import has been successful. If successful there the query will return 0.  
6. Now import the Java project in Eclipse (it was built and tested with Eclipse so we are not sure if it would work easily in different IDE).  
7. Firstly start the FillDataInTheEmptyDatabase.java program located in examples package. This program will populate the database with data fetched from Last FM API. This is is the computationally most expensive part. Here we are crawling data from the API, doing some calculations and storing the data to MySQL. It finished in 4.5 hours by doing it on 4 different laptops and then merging 4 dumps produced. By this "paralelization" we managed to finish the insertion in about 4 hours (the 4 commented calls of insertion should be uncommented on 4 different machines). If someone wants to do it on one single machine, he should just start the class downloaded from GitHub. However, by doing that, he will have to wait about 4x4 = 16 hours.  
8. After doing the insertion, to evaluate the prediction start the CrossValidation.java file located in validation package. This should take same amount of time as in the First approach described in 7. of the First Way.  
