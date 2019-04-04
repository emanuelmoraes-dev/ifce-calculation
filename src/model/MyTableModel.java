package model;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	
	public static class MyKeyAdapterTable extends KeyAdapter {
		private JTable table = null;
		private MyTableModel tableModel = null;
		
		public MyKeyAdapterTable(JTable table, MyTableModel tableModel) {
			this.table = table;
			this.tableModel = tableModel;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			if((int)e.getKeyChar() == 127 && this.tableModel.allowUserDeleteLines) {
				int rowSelected = this.table.getSelectedRow();
				
				if(rowSelected < 0 || rowSelected >= this.table.getRowCount())
					return;
				
				this.tableModel.removeRow(rowSelected);
			}
		}
	}

	private static final long serialVersionUID = 1L;
	
	private List<Object> columns = null;
	private List<List<Object>> rows = null;
	private List<List<Boolean>> cellsEditable = null;
	
	private boolean allowUserAddLines = false;
	private boolean allowUserDeleteLines = false;
	
	private void updateRows() {
		
		if(!allowUserAddLines)
			return;
		
		List<Object> last = null;
		
		if(this.rows.size() > 0) {
			last = this.rows.get(rows.size() - 1);
		
			for(Object cell : last) {
				if(!cell.toString().equals("")) {
					List<Object> rowEmpty = new ArrayList<>();
					
					for(int i = 0; i < this.columns.size(); i++)
						rowEmpty.add("");
					
					this.rows.add(rowEmpty);
					this.fireTableRowsInserted(this.rows.size() - 1, this.rows.size() - 1);
						
					break;
				}
			}
		} else {
			List<Object> rowEmpty = new ArrayList<>();
			
			for(int i = 0; i < this.columns.size(); i++)
				rowEmpty.add("");
			
			this.rows.add(rowEmpty);
			this.fireTableRowsInserted(this.rows.size() - 1, this.rows.size() - 1);
		}
	}
	
	public MyTableModel() {
		this.columns = new ArrayList<>();
		this.rows = new ArrayList<>();
		this.cellsEditable = null;
	}
	
	public MyTableModel(Object[] columns) {
		this.columns = new ArrayList<>();
		
		for(Object col : columns)
			this.columns.add(col);
		
		this.rows = new ArrayList<>();
		this.cellsEditable = null;
		
		updateRows();
	}
	
	public MyTableModel(List<Object> columns) {
		this.columns = columns;
		this.rows = new ArrayList<>();
		this.cellsEditable = null;
		
		updateRows();
	}
	
	public MyTableModel(Object[] columns, List<List<Object>> rows) {
		this.columns = new ArrayList<>();
		
		for(Object col : columns)
			this.columns.add(col);
		
		this.rows = rows;
		this.cellsEditable = null;
		
		updateRows();
	}
	
	public MyTableModel(List<Object> columns, List<List<Object>> rows) {
		this.columns = columns;
		this.rows = rows;
		this.cellsEditable = null;
		
		updateRows();
	}
	
	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columns.get(columnIndex).toString();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(cellsEditable == null)
			return true;
		if(rowIndex >= cellsEditable.size() || rowIndex < 0)
			return true;
		if(columnIndex >= cellsEditable.get(rowIndex).size() || columnIndex < 0)
			return true;
		return cellsEditable.get(rowIndex).get(columnIndex) == true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.rows.get(rowIndex).get(columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.rows.get(rowIndex).set(columnIndex, aValue);
		this.updateRows();
		this.fireTableCellUpdated(rowIndex, columnIndex);
	}

	public List<List<Boolean>> getCellsEditable() {
		return this.cellsEditable;
	}

	public void setCellsEditable(List<List<Boolean>> cellsEditable) {
		this.cellsEditable = cellsEditable;
		this.fireTableStructureChanged();
	}
	
	public void removeColumn(int columnIndex) {
		this.columns.remove(columnIndex);
		
		for(List<Object> row : this.rows)
			row.remove(columnIndex);
		
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}
	
	public void removeColumn() {
		this.removeColumn(this.columns.size() - 1);
	}
	
	public void addColumn(int columnIndex, Object column) {
		this.columns.add(columnIndex, column);
		
		for(List<Object> row : rows)
			row.add(columnIndex, "");
		
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}
	
	public void addColumn(Object column) {
		this.addColumn(columns.size(), column);
	}
	
	public void removeRow(int rowIndex) {
		this.rows.remove(rowIndex);
		this.fireTableRowsDeleted(rowIndex, rowIndex);
		this.updateRows();
	}
	
	public void removeRow() {
		this.removeRow(rows.size() - 1);
	}
	
	public void addRow(int rowIndex, Object[] row) {
		List<Object> listRow = new ArrayList<>();
		
		if(allowUserAddLines && rowIndex == rows.size() - 1) {
			this.rows.remove(rowIndex);
			this.fireTableRowsDeleted(rowIndex, rowIndex);
		}
		
		for(Object cell : row)
			listRow.add(cell);
		
		this.rows.add(rowIndex, listRow);
		
		this.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		this.updateRows();
	}
	
	public void addRow(Object[] row) {
		this.addRow(rows.size() - 1, row);
	}
	
	public void addRow(int rowIndex, List<Object> row) {
		if(allowUserAddLines && rowIndex == rows.size() - 1) {
			this.rows.remove(rowIndex);
			this.fireTableRowsDeleted(rowIndex, rowIndex);
		}
		
		this.rows.add(row);
		this.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		this.updateRows();
	}
	
	public void addRow(List<Object> row) {
		this.addRow(rows.size() - 1, row);
	}

	public boolean isAllowUserAddLines() {
		return this.allowUserAddLines;
	}

	public void setAllowUserAddLines(boolean allowUserAddLines) {
		this.allowUserAddLines = allowUserAddLines;
		
		if(allowUserAddLines)
			updateRows();		
	}

	public boolean isAllowUserDeleteLines() {
		return allowUserDeleteLines;
	}

	public void setAllowUserDeleteLines(boolean allowUserDeleteLines) {
		this.allowUserDeleteLines = allowUserDeleteLines;
	}
}
