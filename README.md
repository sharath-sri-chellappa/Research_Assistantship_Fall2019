**Blockchain based Ticketing Solution for Sporting Arenas**

**ACKNOWLEDGEMENTS**

I would like to thank Professor Priya Narasimhan for helping conceive and bring to conclusion this unique problem statement combining the concepts of Blockchain and Distributed Authentication. This work is first of a kind with no previous work done in this area. Hence any work that we plan to do will be the first in this sphere. The given work has been completed by using Nexus Devices as the Peers in the Network and a One Plus 6T as the main authenticator.

**ABSTRACT**

This project proposes a Blockchain based authentication solution for ticketing in Sporting arenas which would allow ticket holders to carry out a group based authentication once their tickets have been deemed valid and can now go ahead and validate other ticket holders. This group based authentication helps reduce the amount of time being spent on each ticket with a total time to authenticate being reduced to atleast 1/10th of the time of a sequential authentication mechanism.

**PROBLEM STATEMENT AND MOTIVATION**

This problem statement can be better explained with the help of an example. Let us consider the case of the Pittsburgh Penguins stadium. The seating capacity of the stadium is 19,758. Now considering an average of 3 seconds for each ticket being scanned in the stadium, this brings the total time spent on scanning all the tickets are 39516 seconds (659 minutes). This is assuming that there is only one scanner available for scanning each ticket.

Now let us consider the case where in there are 5 entrances into the stadium with 3 scanners being placed at each entrance. Now that would mean that there are 15 scanners to take care of the scanning of the total set of tickets. The total time taken to scan and authenticate each ticket holder would be 33 minutes.

This however is a stadium with a seating capacity of just 20,000. Assuming a seating capacity of 100000, with about 10 entrances and 6 scanners per entrance, it takes us approximately 84 minutes (1hr and 24 mins) for scanning all the tickets in a sold out stadium.

Although 33 minutes / 84 minutes is a fairly good time, in today&#39;s day and age, we must understand means and ways of decreasing the time and improve user experience for the user as it is they who have to wait in order to get in to watch their favorite sport.

Our idea proposes to use the concept of Group Based Authentication in a peer distributed fashion without the involvement of a Central Server. In this case, if a scanner authenticates User 1, he is made a part of the trusted chain thereby giving him access to authenticate anyone post this. Hence the number of authenticators increase on the fly thereby greatly decreasing the time. Considering a small number of ticket holders (say 100), let us assume that we start with just 1 scanner. When 1 ticket holder has been scanned, then the number of scanners are now 2. This way let&#39;s assume that the ratio of a ticket holder to turn into an authenticator is 0.4 (i.e. 4 out of 10 ticket holders go ahead to authenticate other people). In this case, the sequence can then be simplified to the following: Number of scanners (Number of tickets authenticated).

1 (0 authenticated), 2 (2 authenticated), 4 (4 authenticated), 4 (8 authenticated), 6 (12 authenticated), 6 (18 authenticated), 8 (24 authenticated), 12 (32 authenticated), 16 (44 authenticated), 24 (60 authenticated), 32 (84 authenticated), 40 (116 authenticated) and so on.

So if you notice in this case, the amount of time taken to authenticate 100 people (assuming 3 seconds per person), starting out at 1 scanner is going to be 36 seconds. The same scenario without our group-based authentication would be 300 seconds (5 minutes). The time taken in this approach is reduced to 1/10th in comparison to the traditional approach.

**SOLUTION PROPOSED**

Details of our solution are described in this section. We propose a blockchain based authentication technique where in each party in the system is termed as Nodes with there being 2 broad types of each. Node A and Node B. Node A is the primary authenticator in the System. What this means is that the first ever ticket to be authenticated in the system will be authenticated by a Node A peer. Please note that there is only one type of Node A peer in the system. Every node which gets authenticated and can now further authenticate fall in to the Node B category. The Node B first displays the latest block&#39;s QR code which when authenticated allows the Node B&#39;s application to move into the 2nd Screen (QR Code Reader). Details on the architecture are displayed in the next Section. The database used for the POC is the Firebase Database as that is the easiest Real Time Database to interface with the Android Application.

The calculation of the Hash along with the meaning of the word difficulty will be explained in the later sections. In the Project, we are using One Plus 6T as Node A, while using Google Nexus as Node Bs. In the real world scenario, Node A would be a Scanner while Node B would be a ticket holder. As soon as the scanner scans the QR code on the ticket, it would make Node B an Authenticator which can go ahead and authenticate other Node Bs.

**Please note that here Node A and Node B does not refer to 2 nodes but rather to 2 types of Nodes. All nodes of type Node A will be the already authenticated scanners in the stadium. Every ticket holder would be of type Node B. This naming convention will be used henceforth.**

**The details of the implementation can be referred to the Project Report.**

**EVALUATION METHODOLOGY**

There are 2 important aspects to the application that needs to be evaluated strictly:

- Security - The application security has been evaluated against the Open Source Standard which is used by Mobile Security experts namely MobSF.
- Scalability – The application can only be deemed scalable by involving real time scenarios. What I propose is to use this solution in an attendance scenario for the Sports Tech Class next semester. If each student with an Android Phone downloads the application and are the Node Bs in this system, we can have one central authenticator (Node A) to authenticate one node in the system which can then go ahead and authenticate other nodes in the system.

**This idea on scalability is something that I wish to discuss with the professor seriously as it would go a long way in helping with research.**

**RESULTS**

**Because of the lack of reliable scalabilty testing, I have the results only for 4 nodes.**

Time taken through sequential authentication – 12 seconds (3 seconds for each node)

Time taken through group based authentication – 6 seconds (1 Node A and 1 Node B authenticator).

Please note that as the number of nodes and number of authenticators increase there will be a substantial drop off in the time taken for the complete authentication process. This small sample space is an indication of this trend.

**SIMILAR WORK**

The work done in this project is quite unique that it doesn&#39;t have any prior art which is similar to the work done here.

Having said this, the concept of group based Authentication is a direct consequence of the concept of &quot;Crowd Intelligence&quot; which is explained in the paper titled &quot;A Blockchain-enabled Trustless Crowd-Intelligence Ecosystem on Mobile Edge Computing&quot;[1] where in the whole paper talks about how a computationally intensive problem can be split amongst multiple nodes with the work being authorized by a Proof of Work carried out by each node. Taking a leaf out of this idea, I came to an idea where in the work (Authentcation) instead of being carried out by a single main-node, is carried out by multiple nodes, with each node carrying out computationally intensive task (adding and verifying a blockchain) to ascertain their authenticity. This slowly kept changing till I arrived at my idea.

Now with this information, I wanted to understand if Blockchain can be implemented on a resource constrained application like a mobile. On reading resources [2],[3],[4], I saw a potential of using blockchain in these set of devices which is what drove me to go ahead and implement this idea.

**References:**

[1] [https://ieeexplore.ieee.org/document/8632682](https://ieeexplore.ieee.org/document/8632682)

[2] [https://www.ibm.com/blogs/blockchain/2018/10/decentralized-identity-an-alternative-to-password-based-authentication/](https://www.ibm.com/blogs/blockchain/2018/10/decentralized-identity-an-alternative-to-password-based-authentication/)

[3] [https://ieeexplore.ieee.org/document/7917634](https://ieeexplore.ieee.org/document/7917634)

[4] [https://ieeexplore.ieee.org/document/7946872](https://ieeexplore.ieee.org/document/7946872)
