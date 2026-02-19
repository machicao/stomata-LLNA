
load descriptores
FontName='HelvLight';
file = 'fileplotpca.pdf';
k = [1, 1, 1, 1, 1, 2, 2, 2,2,2,3,3,3,3,3];%clases
classes = {'AMB','CO2','UM90'};

%o # classes deve ser menor q os descritores, manova dando erro, mas concertei usando 
%1ro os descriptores PCA

descriptoresnorm = normalize(descriptores);
[A V] = runPCA(descriptoresnorm);
trainProjPCA = descriptoresnorm *V;%all components projection
[d, p, stats]= manova1(trainProjPCA(:,1:12),k);%sao 36

c1= stats.canon(:,1);
c2= stats.canon(:,2);
figure(1)
gscatter(c1(:,1),c2(:,1),k,'rgb','ooo')
xlabel('1st canonical variavel');
ylabel('2nd canonical variavel');
legend(classes)
legend('Location','NorthWest');
set(gca,...
    'FontUnits','points',...
    'FontWeight','normal',...
    'FontSize',10,...
    'FontName','HelvLight')
xlim([-12 16])
ylim([-7 7])

export_fig(file, '-q600', '-transparent');
%%hold off;



