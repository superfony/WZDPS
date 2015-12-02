package com.epsmart.wzdp.activity.contract.fragment.fsch;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;

public class FileExplorer implements OnItemClickListener {
	private static String ROOT_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/** 文件夹不为空 */
	private static final int FOLDER_HAS_CHILD = 0;
	/** 文件夹为空 */
	private static final int FOLDER_IS_EMPTY = 1;
	/** 文件夹不可读 */
	private static final int FOLDER_ISNT_READ = 2;

	private Context context;
	protected String currentPath;

	private boolean initialized;

	private TextView folderNameText;
	private ListView fileListView;

	protected boolean showPlainFiles;

	private FileItemAdapter fileItemAdapter;

	private FileNamesComparatorFoldersUp comparatorFoldersUp;
	private FileNamesComparatorFoldersNotUp comparatorFoldersNotUp;

	private Thread fileLoadThread;
	private FileLoadHandler fileLoadHandler;

	private String loadingPathName;
	private List<File> fileItems;

	private LayoutInflater LayoutInflater;
	private ViewGroup explorerRootView;
	private Dialog explorerDialog;
	private static FileExplorer fileExplorer = null;

	private FileExplorerListener listener;

	public interface FileExplorerListener {
		void onClosed(List<String> selectedFiles);
	}

	private FileExplorer(Context context, FileExplorerListener listener) {
		this.context = context;
		this.listener = listener;
	}

	public static FileExplorer openFileExplorer(Context context,
			FileExplorerListener listener, String rootPath) {
		if (null != rootPath)
			ROOT_DIRECTORY = rootPath;
		fileExplorer = new FileExplorer(context, listener);
		fileExplorer.init();
		return fileExplorer;
	}

	private class FileNamesComparatorFoldersUp implements Comparator<File> {

		public int compare(File left, File right) {
			if (left.isDirectory()) {
				if (right.isDirectory()) {
					return left.compareTo(right);
				}
				return -1;
			}
			return right.isDirectory() ? 1 : left.compareTo(right);
		}
	}

	private class FileNamesComparatorFoldersNotUp implements Comparator<File> {
		public int compare(File left, File right) {
			return left.compareTo(right);
		}
	}

	private class LoadFilesThread extends Thread {
		public void run() {
			int result = _showDirectoryContents();
			Message message = fileLoadHandler.obtainMessage();
			message.what = 1;
			message.arg1 = result;
			fileLoadHandler.sendMessage(message);
		}
	}

	private static class FileLoadHandler extends Handler {
		private WeakReference<FileExplorer> reference;

		FileLoadHandler(FileExplorer explorer) {
			reference = new WeakReference<FileExplorer>(explorer);
		}

		public void handleMessage(Message message) {
			FileExplorer explorer = reference.get();
			if (message.what == 1) {
				if (message.arg1 == FOLDER_HAS_CHILD) {
					explorer.showContent();
				} else if (message.arg1 == FOLDER_IS_EMPTY) {
					explorer.isEmpty();
				} else if (message.arg1 == FOLDER_ISNT_READ) {
					explorer.isntRead();
				}
			}
		}
	}

	private void showContent() {
		showDirectoryContentsUI();
		fileLoadThread = null;
	}

	private void isEmpty() {
		ToastTool.toast(context,
				context.getResources().getString(R.string.folder_is_empty));
		fileLoadThread = null;
	}

	private void isntRead() {
		ToastTool.toast(
				context,
				loadingPathName
						+ ","
						+ context.getResources().getString(
								R.string.folder_isnt_readable));
		fileLoadThread = null;
	}

	private void init() {
		LayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		explorerRootView = (ViewGroup) LayoutInflater.inflate(
				R.layout.fileexplorer, null);
		Button button = (Button) explorerRootView.findViewById(R.id.select);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				explorerDialog.dismiss();
				if (null != listener) {
					SparseBooleanArray checkedStat = fileItemAdapter
							.getCheckedStat();
					List<String> list = new ArrayList<String>();
					int size = checkedStat.size();
					for (int i = 0; i < size; i++) {
						if (checkedStat.get(i)) {
							list.add(fileItemAdapter.getFileItems().get(i)
									.getAbsolutePath());
						}
					}
					listener.onClosed(list);
				}
			}
		});
		explorerDialog = new Dialog(context, R.style.dialog_fileexplore);
		explorerDialog.setContentView(explorerRootView);
		// explorerDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		//
		// @Override
		// public boolean onKey(DialogInterface dialog, int keyCode,
		// KeyEvent event) {
		// if ((keyCode == KeyEvent.KEYCODE_BACK)
		// && (!currentPath.equals(""))) {
		// showDirectoryContents(getParent(currentPath));
		// return true;
		// }
		// return false;
		// }
		// });

		currentPath = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("root_folder", ROOT_DIRECTORY);
		fileLoadThread = null;

		fileLoadHandler = new FileLoadHandler(this);
		fileItems = new ArrayList<File>();

		initialized = false;
		showPlainFiles = true;

		folderNameText = (TextView) explorerRootView
				.findViewById(R.id.folder_name);
		fileListView = (ListView) explorerRootView
				.findViewById(R.id.files_listview);
		fileListView.setOnItemClickListener(this);

		comparatorFoldersUp = new FileNamesComparatorFoldersUp();
		comparatorFoldersNotUp = new FileNamesComparatorFoldersNotUp();

		fileItemAdapter = new FileItemAdapter(new ArrayList<File>());
	}

	public void show() {
		if (!initialized) {
			initialized = true;
			showDirectoryContents(currentPath);
		}
		explorerDialog.show();
	}

	public void hide() {
		if (null != explorerDialog && explorerDialog.isShowing())
			explorerDialog.dismiss();
	}

	private String getParent(String pathname) {
		int index = pathname.lastIndexOf("/");
		if (index <= 0)
			return "";
		return pathname.substring(0, index);
	}

	private void showDirectoryContentsUI() {
		currentPath = loadingPathName;
		folderNameText.setText(currentPath + "/");
		fileItemAdapter.removeAll();
		fileItemAdapter.addAll(fileItems);
		fileListView.setAdapter(fileItemAdapter);
	}

	private int _showDirectoryContents() {
		File folder = new File(loadingPathName + "/");
		File[] childs = folder.listFiles();
		if ((childs == null) || (childs.length == 0)) {
			if (folder.canRead()) {
				return FOLDER_IS_EMPTY;
			} else {
				return FOLDER_ISNT_READ;
			}
		} else {
			File file = new File("");
			fileItems.add(file);
			for (File child : childs) {
				if ((showPlainFiles) || (child.isDirectory()))
					fileItems.add(child);
				
			}
			if (PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean("list_folders_first", true)) {
				Collections.sort(fileItems, comparatorFoldersUp);
			} else {
				Collections.sort(fileItems, comparatorFoldersNotUp);
			}
			return 0;
		}
	}

	private void showDirectoryContents(String pathname) {
		if (fileLoadThread == null) {
			loadingPathName = pathname;
			fileItemAdapter.checkedAll(false);
			fileItems.clear();
			fileLoadThread = new LoadFilesThread();
			fileLoadThread.start();
		}
	}

	public boolean isFolder(String pathname) {
		File file = new File(pathname);
		return file.isDirectory();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		String filename;

		if (position == 0) {
			if (currentPath.equals("")) {
				filename = String.format("%s/%s", currentPath, fileItemAdapter
						.getFileItems().get(position).getName());
			} else {
				filename = getParent(currentPath);
			}
		} else {
			filename = String.format("%s/%s", currentPath, fileItemAdapter
					.getFileItems().get(position).getName());
			if (!isFolder(filename)) {
				Toast.makeText(context, R.string.is_not_a_folder,
						Toast.LENGTH_SHORT).show();
				return;
			}
		}

		showDirectoryContents(filename);
	}

	class FileItemAdapter extends BaseAdapter {
		private SparseBooleanArray checkedStat;
		private List<File> fileItems;
		private DisplayMetrics dm;

		FileItemAdapter(List<File> fileItems) {
			this.fileItems = fileItems;
			checkedStat = new SparseBooleanArray();
			checkedAll(false);

			dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
		}

		public void addAll(List<File> fileItems2) {
			fileItems.addAll(fileItems2);
			checkedAll(false);
		}

		public void checkedAll(Boolean checked) {
			for (int i = fileItems.size() - 1; i >= 0; i--) {
				setChecked(i, checked, false);
			}
			this.notifyDataSetChanged();
		}

		public void setChecked(int position, Boolean checked,
				boolean notifyDataSetChanged) {
			checkedStat.put(position, checked);
			if (notifyDataSetChanged)
				this.notifyDataSetChanged();
		}

		public void removeAll() {
			fileItems.clear();
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return fileItems.size();
		}

		@Override
		public Object getItem(int position) {
			return fileItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				holder = new ViewHolder();
				convertView = LayoutInflater.inflate(R.layout.fileexplorer_row,
						null);
				holder.fileIcon = (ImageView) convertView
						.findViewById(R.id.fileIcon);
				holder.fileName = (TextView) convertView
						.findViewById(R.id.fileName);
				holder.fileSelect = (CheckBox) convertView
						.findViewById(R.id.fileSelect);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			File file = fileItems.get(position);
			if (position == 0){
				Log.d("", "....position==0...>>"+currentPath);
				if (!currentPath.equals("")) {
					holder.fileIcon.setBackgroundResource(0);
					holder.fileIcon.setImageResource(R.drawable.folder_up);
					holder.fileName.setText(context.getResources().getString(
							R.string.folder_up));
					holder.fileSelect.setVisibility(View.INVISIBLE);
				} 
			} else {
				setItem(holder, file, position);
			}
			return convertView;
		}

		private void setItem(ViewHolder holder, File file, int position) {
			if (file.isDirectory()) {
//				ImageManager2.from(context).displayImage(holder.fileIcon, "",
//						R.drawable.folder, 100, 100);
				holder.fileIcon.setBackgroundResource(0);
//				holder.fileIcon.setImageBitmap(null);
//				holder.fileIcon.setImageDrawable(null);
				holder.fileIcon.setImageResource(R.drawable.folder_up);
			} else {
				ImageManager2.from(context).displayImage(holder.fileIcon,
						file.getAbsolutePath(), R.drawable.file, 100, 100);
			}
			holder.fileName.setText(file.getName());
			holder.fileSelect.setVisibility(file.isDirectory() ? View.INVISIBLE
					: View.VISIBLE);
			holder.fileSelect.setTag(position);
			holder.fileSelect
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							Integer pos = (Integer) buttonView.getTag();
							checkedStat.put(pos, isChecked);
						}
					});
			holder.fileSelect.setChecked(file.isDirectory() ? false
					: checkedStat.get(position));
		}

		class ViewHolder {
			ImageView fileIcon;
			TextView fileName;
			CheckBox fileSelect;
		}

		public void setFileItems(List<File> fileItems) {
			this.fileItems = fileItems;
		}

		public List<File> getFileItems() {
			return fileItems;
		}

		public SparseBooleanArray getCheckedStat() {
			return checkedStat;
		}

		public int dipToPx(int dip) {
			return (int) (dip * dm.density + 0.5f);
		}
	}
}