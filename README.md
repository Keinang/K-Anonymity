# K-Anonymity Simulator for Social Networks
A simulator for preserving anonymity on social networks.
  
## How to run  
* Run Main.main()

## Algorithms  
### K-Degree Generalization 
* **Based on**: K. Liu and E. Terzi. Towards identity anonymization on graphs.
* **Adversary background knowledge**: knows the degree of a target victim.
* **Problem**: The victim may be re-identified from a social network even if the victim’s identity is preserved using the conventional anonymization techniques.
* **Solution**: Any vertex in G cannot be re-identified in anonymized G with confidence larger than 1/k.

### K-Symmetry
* **Based on**: Wu, Xiao, Wang, He and Wang, K-Symmetry model for Identify Anonymization in Social Networks
* **Adversary background knowledge**: knows a combination of multiple easily obtained structural knowledge. 
* **Problem**: it’s very difficult for the network data publishers to make such predication since there exists numerous possible structural knowledge. 
A combination of multiple easily obtained structural knowledge could have quite strong descriptive power, which can re-identify a large fraction of individuals from the network. 
So a K-anonymity model independent of structural knowledge use is necessary.
* **Solution**: The general idea is to modify the network so that for each vertex v there exist at least k− 1 other vertices each of which serves as the image of v under some automorphism of the modified network. 
Informally speaking, an automorphism of a network is a permutation on its vertices which preserves its vertex adjacency relationships.

## Data Sets  
### Facebook circles  
* **URL**:      https://snap.stanford.edu/data/egonets-Facebook.html  
* **File**:     facebook_combined.txt  
* **Nodes**:	4039   
* **Edges**:	88234  

### Arxiv Collaboration network 
* **URL:      https://snap.stanford.edu/data/ca-GrQc.html  
* **File****:     Arxiv_collaboration_network.txt  
* **Nodes**:	5242  
* **Edges**:	28980

### Wiki votes  
* **URL**:      https://snap.stanford.edu/data/wiki-Vote.html  
* **File**:     wiki-Vote.txt 
* **Nodes**:	7115  
* **Edges**:	103689    