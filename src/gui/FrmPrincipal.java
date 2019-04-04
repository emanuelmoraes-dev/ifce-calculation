package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import model.MyTableModel;
import util.Validate;

public class FrmPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_QUANT_NOTAS = 4;
	private static final Object[] DEFAULT_PESOS = new Object[] {2, 2, 3, 3};
	
	private JTable table = null;
	private MyTableModel tableModel = null;
	private JScrollPane scroll = null;
	private JButton btnCalcular = null;
	private JLabel lblQuantNotas1 = null;
	private JLabel lblQuantNotas2 = null;
	private JSpinner spiQuantNotas = null;
	private JLabel lblMedia = null;
	private JSpinner spiMedia = null;
	
	private int quantNotas = DEFAULT_QUANT_NOTAS;

	public FrmPrincipal() {
		init();
	}
	
	private void init() {
		// Objeto que trata os eventos
		Events events = new Events();
		
		// Modificando o tamanho da janela
		this.setSize(500, 400);
		
		// Modificando título da janela
		this.setTitle("IFCE Calculation 2.0");
		
		// Definindo ação do botão de fechar
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		// Centralizando janela
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(tela.width /2 - this.getWidth()/2, 
						 tela.height /2 - this.getHeight()/2);
		
		//Configurando layout e adicionando o painel ao contentPane
		
		JPanel pane = new JPanel();
		pane.setBackground(Color.ORANGE);
		
		{
			GroupLayout groupLayout = new GroupLayout(this.getContentPane());
			
			groupLayout.setHorizontalGroup(
					groupLayout.createParallelGroup()
						.addComponent(pane, 0, 498, Short.MAX_VALUE)
			);
			
			groupLayout.setVerticalGroup(
					groupLayout.createParallelGroup()
						.addComponent(pane, 0, 371, Short.MAX_VALUE)
			);
			
			this.getContentPane().setLayout(groupLayout);
		}
		
		//Configurando layout e adicionando elementos ao painel
		
		this.table = new JTable();
		this.tableModel = new MyTableModel(new Object[] {"Nota", "Peso"}) {
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				
				if(columnIndex == 1 && aValue.toString().equals("")) {
					JOptionPane.showMessageDialog(FrmPrincipal.this, "O campo de peso não deve estar vazio");
					return;
				}
				
				if(!aValue.toString().equals("") && !Validate.isDouble(aValue.toString())) {
					JOptionPane.showMessageDialog(FrmPrincipal.this, "Error: Todas as células devem conter apenas números");
					return;
				}
				
				super.setValueAt(aValue, rowIndex, columnIndex);
			}
		};
		
		for(int i = 0 ; i < DEFAULT_QUANT_NOTAS; i++) {
			List<Object> list = new ArrayList<>();
			list.add("");
			list.add(DEFAULT_PESOS[i]);
			this.tableModel.addRow(list);
		}
		
		this.table.setModel(tableModel);
		this.table.addKeyListener(new MyTableModel.MyKeyAdapterTable(table, tableModel));
		((DefaultTableCellRenderer)this.table.getDefaultRenderer(String.class)).setHorizontalAlignment(JLabel.CENTER);
		
		this.scroll = new JScrollPane();
		this.scroll.setViewportView(this.table);
		
		this.btnCalcular = new JButton("Calcular");
		this.btnCalcular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				events.btnCalcular_actionPerformad(evt);
			}
		});
		
		this.lblQuantNotas1 = new JLabel("Quantidade máxima de notas");
		this.lblQuantNotas2 = new JLabel("que você espera obter:");
		
		this.spiQuantNotas = new JSpinner(new SpinnerNumberModel(DEFAULT_QUANT_NOTAS, 0, null, 1));
		this.spiQuantNotas.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				events.spiQuantNotas_stateChanged(evt);
			}
		});
		
		this.lblMedia = new JLabel("Média Escolar:");
		this.spiMedia = new JSpinner(new SpinnerNumberModel(new Double(7), new Double(0), null, new Double(1)));
		
		GroupLayout groupLayout = new GroupLayout(pane);
		groupLayout.setHorizontalGroup(
			groupLayout.createSequentialGroup()
				.addGap(50)
				.addComponent(scroll, 0, 0, Short.MAX_VALUE)
				.addGap(20)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(lblQuantNotas1)
					.addComponent(lblQuantNotas2)
					.addComponent(spiQuantNotas, GroupLayout.DEFAULT_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addComponent(lblMedia)
					.addComponent(spiMedia, GroupLayout.DEFAULT_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addComponent(btnCalcular))
				.addGap(20)
		);
		
		groupLayout.setVerticalGroup(
			groupLayout.createSequentialGroup()
				.addGap(20)
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(scroll, 0, 0, Short.MAX_VALUE)
					.addGroup(groupLayout.createSequentialGroup()
						.addComponent(lblQuantNotas1)
						.addComponent(lblQuantNotas2)
						.addGap(5)
						.addComponent(spiQuantNotas, GroupLayout.DEFAULT_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addGap(15)
						.addComponent(lblMedia)
						.addGap(5)
						.addComponent(spiMedia, GroupLayout.DEFAULT_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addGap(20)
						.addComponent(btnCalcular)))
				.addGap(20)
		);
		
		pane.setLayout(groupLayout);
	}

	public static void main(String[] args) {
		try {
			for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				//System.out.println(info.getName());
				if("Metal".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				FrmPrincipal principal = new FrmPrincipal();
				principal.setVisible(true);
			}
		});
	}	

	private class Events {
		public void spiQuantNotas_stateChanged(ChangeEvent evt) {
			// Ao mudar a quantidade de notas
			
			int quant = Integer.parseInt(FrmPrincipal.this.spiQuantNotas.getValue().toString());
			int dif = quant - FrmPrincipal.this.quantNotas;
			
			if(dif >= 0) {
				for(int i = 0; i < dif; i++) {
					List<Object> list = new ArrayList<>();
					list.add("");
					list.add(1);
					FrmPrincipal.this.tableModel.addRow(list);
				}
			} else {
				dif = -dif;
				for(int i = 0; i < dif; i++)
					FrmPrincipal.this.tableModel.removeRow();
			}
			
			FrmPrincipal.this.quantNotas = quant;
		}
		
		public void btnCalcular_actionPerformad(ActionEvent evt) {
			// Ao Clicar no botão de calcular
			
			double vun = 0;
			double vua = 0;
			double sp = 0;
			double spf = 0;
			boolean falta = false;
			
			for(int i = 0; i < FrmPrincipal.this.tableModel.getRowCount(); i++) {
				String strNota = FrmPrincipal.this.tableModel.getValueAt(i, 0).toString();
				double peso = Double.parseDouble(FrmPrincipal.this.tableModel.getValueAt(i, 1).toString());
				
				if(Validate.isDouble(strNota)) {
					double nota = Double.parseDouble(strNota);
					vua += (nota*peso);
				} else {
					falta = true;
					spf += peso;
				}
				
				vun += peso;
			}
			
			sp = vun;
			
			double media = Double.parseDouble(FrmPrincipal.this.spiMedia.getValue().toString());
			vun = media * vun;
			double vuf = vun - vua;
			
			if(falta) {
				double notaNecessaria = vuf / spf;
				
				for(int i = 0; i < FrmPrincipal.this.tableModel.getRowCount(); i++) {
					String strNota = FrmPrincipal.this.tableModel.getValueAt(i, 0).toString();
					
					if(strNota.equals(""))
						FrmPrincipal.this.tableModel.setValueAt(String.format("%.3f", notaNecessaria).replace(',', '.'), i, 0);
				}
			} else {
				double mediaFinal = vua / sp;
				JOptionPane.showMessageDialog(FrmPrincipal.this, "Sua Média Final Foi " + String.format("%.3f", mediaFinal).replace(',', '.'));
			}
		}
	}
}
