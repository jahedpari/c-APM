# ACPM (Artificial Prediction Markets for Online Prediction)

For more information about the model and its usage, kilndly refer to this paper:
Jahedpari, F., Rahwan, T., Hashemi, S., Michalak, T. P., De Vos, M., Padget, J., & Woon, W. L. (2017). Online Prediction via Continuous Artificial Prediction Markets. IEEE Intelligent Systems, 32(1), 61-68.


This folder contains 

1) “src” folder: includes the source codes.
2) “Data” folder: includes the data files
Note: In order to save time,  a number of classifier is trained beforehand and their prediction for each record is save in  data files (i.e each column refers to prediction of one distinct model).  Then, we assign one distinct column to each agent. In this way, agents do not require retraining their model every time when run a new experiment and hence save running time.  
 However, the program has the capability of
•	using the original data sets ( before training and prediction by a model)
•	assigning a model to each agent  
•	training agents with their model, for each record.

3) Results and summery of each experiment is automatically being saved in “Results” folder.

Since this code is
•	being incrementally generated over my 3.5 year PhD, 
•	has gone through many changes  
•	being used for different type of experiments,  
you may find some unnecessary lines of code with regard to this specific paper.


In case you have a comment o find this code useful, please let me know!

Thanks
