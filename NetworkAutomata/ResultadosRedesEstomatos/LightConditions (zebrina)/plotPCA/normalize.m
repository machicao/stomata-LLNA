function xNorm = normalize(X)
% [N ~] = size(X);
% AMean = mean(X);
% AStd = std(X);
% 
% xNorm = (X - repmat(AMean,[N 1])) ./ repmat(AStd,[N 1]);%standardization
% 
minI = min(X(:));
maxI = max(X(:));
%% normalize between [0...1]
xNorm = X - minI;
xNorm = xNorm ./ (maxI - minI);
