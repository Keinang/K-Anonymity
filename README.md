# K-Anonymity 
A simulator for preserving anonymity on social networks.
  
## How to run  
* Run Main.main()

## Algorithms  
### K-Degree Generalization 
* Based on: K. Liu and E. Terzi. Towards identity anonymization on graphs.
* Adversary background knowledge: knows the degree of a target victim.
* Problem: The victim may be re-identified from a social network even if the victim’s identity is preserved using the conventional anonymization techniques.
* Solution: Any vertex in G cannot be re-identified in anonymized G with confidence larger than 1/k.

### K-Neighborhood Anonymity
* Based on: B. Zhou and J. Pei. Preserving privacy in social networks against neighborhood attacks
* Adversary background knowledge: knows the neighbors of a target victim and the relationship among the neighbors.
* Problem: The victim may be re-identified from a social network even if the victim’s identity is preserved using the conventional anonymization techniques.
* Solution: Any vertex in G cannot be re-identified in anonymized G with confidence larger than 1/k.

## Data Sets  
### Facebook circles  
* https://snap.stanford.edu/data/egonets-Facebook.html  
* facebook_combined.txt  
* Nodes	4039   
* Edges	88234  

### Wiki votes  
* https://snap.stanford.edu/data/wiki-Vote.html  
* wiki-Vote.txt 
* Nodes	7115  
* Edges	103689  

### Twitter circles  
* https://snap.stanford.edu/data/egonets-Twitter.html  
* twitter_combined.txt  
* Nodes	81306  
* Edges	1768149  