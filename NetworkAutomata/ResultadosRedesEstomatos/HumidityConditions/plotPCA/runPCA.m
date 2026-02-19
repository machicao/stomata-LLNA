function  [D, Wpca]  = runPCA(X)
[N,~]=size(X);
K=(1/(N-1))*(X')*X;%COVARIANCIA
% K
[V D] = eig(K);
% V

diagonal =diag(D);
[~,idx] = sort(diagonal,'descend'); %sort EigenValues
Wpca =  V(:,idx);
% idx

