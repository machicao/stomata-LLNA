ImageFontSize=14;
LegendFontSize=10;
FileLabel='WithFormatting';
FontName='Garamond';
AxisFontName='CMU Serif';
%dados originais
load descriptores
clase = [1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3,3];%clases
classes = {'Natural','L4','L24'};


descriptoresnorm = normalize(descriptores);
[A V] = runPCA(descriptoresnorm);
trainProjPCA = descriptoresnorm *V(:,1:2);%1 2 componentes, projection

% plot the shape descriptors

figure(1); 
set(gca,'FontName',AxisFontName,'FontSize',ImageFontSize)
gscatter(trainProjPCA(:,1),trainProjPCA(:,2),clase,'krb','ooo',ones(1,numel(clase))*10);
title('medias [s,wl,lz]','fontsize',ImageFontSize);
xlabel('1st principal component','fontsize',ImageFontSize);
ylabel('2nd principal component','fontsize',ImageFontSize);
legend(classes,'Location','northwest');
legend('Location','NorthEast');
file = 'fileplotpca.pdf';

xlhand=get(gca,'xlabel');
ylhand=get(gca,'ylabel');
set(xlhand,'FontName',FontName);
set(ylhand,'FontName',FontName);

%export_fig(file, '-q600', '-transparent');
%hold off;

%%
load descriptores
k = [1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3,3];%clases
legs = {'Natural','L4','L24'};
%o # classes deve ser menor q os descritores, manova dando erro, mas concertei usando 
%1ro os descriptores PCA
[A V] = runPCA(descriptoresnorm);
trainProjPCA = descriptoresnorm *V;%all components projection
[d, p, stats]= manova1(trainProjPCA(:,1:3),k);%sao 48

c1= stats.canon(:,1);
c2= stats.canon(:,2);
figure(1)
gscatter(c1(:,1),c2(:,1),k,'rgb','ooo')
xlabel('1st canonical component');
ylabel('2nd canonical component');
legend(legs)
legend('Location','SouthWest');
%export_fig(file, '-q600', '-transparent');
%hold off;



