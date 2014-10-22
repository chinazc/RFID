package com.cw.assetsrfid.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.cw.assetsrfid.R;
import com.neil.myandroidtools.log.Log;

public class FileUtils {

	/**
	 * 备份还原数据时使用 获取sdcard的路径：外置和内置
	 * 
	 * @param context
	 * @return
	 */
	public static String[] getVolumePaths(Context context) {
		String[] paths = null;
		StorageManager sm = (StorageManager) context
				.getSystemService(Context.STORAGE_SERVICE);
		try {
			paths = (String[]) sm.getClass().getMethod("getVolumePaths")
					.invoke(sm);
		} catch (IllegalArgumentException e) {
			Log.e("getVolumePaths1:", e);
			return paths;
		} catch (IllegalAccessException e) {
			Log.e("getVolumePaths2:", e);
			return paths;
		} catch (InvocationTargetException e) {
			Log.e("getVolumePaths3:", e);
			return paths;
		} catch (NoSuchMethodException e) {
			Log.e("getVolumePaths4:", e);
			return paths;
		} finally {
			if (paths == null) {
				String sdPath = getSDCardPath();
				if (sdPath != null) {
					paths = new String[1];
					paths[0] = sdPath;
				}
			}
		}
		return paths;
	}

	/**
	 * 删除文件和目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {

		if (!file.exists()) {// 文件不存在
			return false;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File temp = files[i];
				boolean result = deleteFile(temp);
				if (!result)
					return false;
			}
		}
		return file.delete();
	}

	/**
	 * @param context
	 * @return 备份目录
	 */
	@SuppressWarnings("unused")
	private static String getBackupDir(Context context) {
		return pathDir(context, R.string.key_path_backup);
	}

	/**
	 * @param context
	 * @return 备份目录
	 */
	public static String getBackupDir() {
		return getSDCardPath();
	}

	/**
	 * @param context
	 * @return Cache目录
	 */
	public static String getCacheDir(Context context) {
		return pathDir(context, R.string.key_path_cache);
	}

	/**
	 * 获取目录并创建子目录
	 * 
	 * @param context
	 * @param resource
	 * @return
	 */
	public static String pathDir(Context context, int resource) {
		return pathDir(context, context.getString(resource));
	}

	/**
	 * 获取目录并创建子目录
	 * 
	 * @param subDir
	 * @return
	 */
	public static String pathDir(Context context, String subDir) {
		String rootPath = getRootDir(context);

		StringBuffer path = new StringBuffer();
		path.append(rootPath);
		path.append(File.separator);
		path.append(subDir);

		String dirPath = path.toString();
		createDir(dirPath);

		return dirPath;
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static void createDir(String path) {
		File file = new File(path);
		if (!file.exists()) { // 不存在
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) { // 父目录不存在
				createDir(parentFile.getAbsolutePath());
			}
			file.mkdir();
		}
	}

	/**
	 * 默认的扩展空间
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		String sdPath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		}
		return sdPath;
	}

	/**
	 * 获取文件保存的父目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getRootDir(Context context) {
		StringBuilder path = new StringBuilder();
		// sd能够使用
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			path.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
		} else {
			path.append(context.getFilesDir().getAbsolutePath());
		}
		path.append(File.separator);
		path.append(context.getString(R.string.key_path_root));

		return path.toString();
	}

	/**
	 * 赋予文件存在权限
	 * 
	 * @param file
	 * @return
	 */
	public static boolean chmodFile(File file) {
		return runCommand("chmod 777 " + file.getAbsolutePath());
	}

	/**
	 * 赋予文件存在权限
	 * 
	 * @param file
	 * @return
	 */
	public static boolean chmodFile(String filePath) {
		return runCommand("chmod 777 " + filePath);
	}

	/**
	 * 设置文件权限
	 * 
	 * @param command
	 * @return
	 */
	public static boolean runCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			Log.e("runCommand:", e);
			return false;
		} finally {
			try {
				process.destroy();
			} catch (Exception e) {
				Log.e("process.destroy:", e);
			}
		}
		return true;
	}

	/**
	 * 获取路径下的文件，不是文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getFileList(String path) {
		File[] res = null;
		List<File> temp = new ArrayList<File>();
		File dirFile = new File(path);
		if (dirFile.isDirectory()) {
			File[] files = dirFile.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					File srcFile = files[i];
					if (srcFile.isFile()) {
						temp.add(srcFile);
					}
				}
			}
		}
		res = temp.toArray(new File[] {});
		return res;
	}

	/**
	 * 移动目录下的所有文件
	 * 
	 * @param srcPath
	 *            原始目录
	 * @param destPath
	 *            目标目录
	 */
	public static void renameAllFile(String srcPath, String destPath) {
		File[] srcFiles = getFileList(srcPath);
		if (srcFiles != null && srcFiles.length > 0) {
			for (File file : srcFiles) {
				File dest = new File(destPath, file.getName());
				file.renameTo(dest);
				file.delete();
			}
		}
	}

	public static boolean copyFile(File srcFile, File destFile)
			throws IOException {
		boolean result = false;
		if (destFile != null && srcFile != null && srcFile.exists()) {
			if (!destFile.exists()) {
				if (!destFile.createNewFile()) {
					return result = false;
				}
			}

			FileInputStream in = new FileInputStream(srcFile);
			FileOutputStream out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				out.flush();
			}
			out.close();
			in.close();
			result = true;
		}
		return result;
	}

//	public static void sortName(File[] files) {
//		sortName(Arrays.asList(files));
//	}
//
//	public static void sortName(List<File> files) {
//		Collections.sort(files, new Comparator<File>() {
//
//			@Override
//			public int compare(File o1, File o2) {
//				String name_o1 = o1.getName();
//				String name_o2 = o2.getName();
//
//				int len_o1 = name_o1.length();
//				int len_o2 = name_o2.length();
//				if (len_o1 > len_o2) {
//					name_o2 = Utils.addZero(name_o2, len_o1);
//				} else if (len_o1 < len_o2) {
//					name_o1 = Utils.addZero(name_o1, len_o2);
//				}
//
//				return name_o1.compareTo(name_o2);
//			}
//		});
//	}

}
