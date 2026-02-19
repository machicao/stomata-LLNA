clear all
clc
close all

WIDTH = 2560;
HIGH = 1920;
escala = .347;

path = '';   % ajuste se necessário

% -------------------------------
% Lista de arquivos
% -------------------------------
files = { 
    'ceth24h1.310xL.txt'
    'ceth4h1.310xL.txt'
};

% -------------------------------
% Parâmetros de threshold
% -------------------------------
threI= 80/escala;
threF= 160/escala;  
threSteps= 20/escala;
thresholds = threI:threSteps:threF;
numsubplots = numel(thresholds);

% -------------------------------
% Loop pelos arquivos
% -------------------------------
for f = 1:numel(files)

    file = files{f};
    folha = importdata([path file]);

    idx = 1;

    FigHandle = figure('Position', [10, 100, 1700, 200], 'Color', 'w');

    for threshold = thresholds

        if idx == 1
            ylabel(file, 'Interpreter', 'none')
        end

        % ---------------------------
        % Desenhar arestas
        % ---------------------------
        ax = subplot(1, numsubplots, idx);
        hold(ax, 'on');

        for i = 1:size(folha,1)-1
            for j = i+1:size(folha,1)
                x1 = folha(i,1); y1 = folha(i,2);
                x2 = folha(j,1); y2 = folha(j,2);

                dist = sqrt((x1-x2)^2 + (y1-y2)^2);

                if dist <= threshold
                    subplot(1, numsubplots, idx)
                    plot([x1,x2], [y1,y2], ...
                        'Color', [.5 .5 .5], ...
                        'LineWidth', .5)
                    hold on
                end
            end
        end
        % --- CORREÇÃO DE ORIENTAÇÃO ---
        %set(gca, 'YDir','reverse'); % Inverte o eixo Y para coincidir com coordenadas de imagem
        %axis equal; % Mantém a proporção 1:1 para não distorcer a geometria
        set(ax, 'YDir', 'reverse');      % Espelhado vertical (coordenada de imagem)
        set(ax, 'Color', 'w');           % Fundo do subplot branco
        set(ax, 'XColor', 'k', 'YColor', 'k'); % Eixos em preto (k = black)
        axis(ax, 'equal');               % Mantém proporção geométrica
        grid(ax, 'off');                 % Garante que não haja grades
        box(ax, 'on');                   % Coloca a moldura no gráfico
    
        % ------------------------------
        
        % ---------------------------
        % Plotar nós
        % ---------------------------
        subplot(1, numsubplots, idx)
        plot(folha(:,1), folha(:,2), 'og', ...
            'LineWidth', 0.8, ...
            'MarkerSize', 3, ...
            'MarkerEdgeColor', 'g', ...
            'MarkerFaceColor', [0 0 0])
        %title(['Thr=' num2str(threshold * escala) ' \mum'])
        % Convertendo o threshold de volta para micrometros para o título
        title_str = sprintf('T_l = %.0f \\mu m', threshold * escala);
        title(ax, title_str, 'FontWeight', 'bold', 'Color', 'k', 'FontSize', 10);

        set(gca, ...
            'FontUnits','points', ...
            'FontWeight','normal', ...
            'FontSize',10, ...
            'FontName','HelvLight')

        xlim([1 WIDTH])
        ylim([1 HIGH])
        axis equal
        box on

        idx = idx + 1;
    end

    % ---------------------------
    % Exportar figura
    % ---------------------------
    [~, name, ~] = fileparts(file);
    exportgraphics(gcf, [name '.pdf'], ...
        'ContentType', 'vector', ...
        'BackgroundColor', 'none', ...
        'Resolution', 300);

    close(FigHandle)
end