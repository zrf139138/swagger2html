package com.gen.helper;

import java.io.File;

/**
 * @author Zero
 * @date 2020-10-22 13:34.
 */
public class DirectoryHelper {

	/**
	 * 迭代删除文件夹
	 * @param dirPath 文件夹路径
	 */
	public static void deleteDir(String dirPath)
	{
		File file = new File(dirPath);
		if(file.isFile())
		{
			file.delete();
		}else
		{
			File[] files = file.listFiles();
			if(files == null)
			{
				file.delete();
			}else
			{
				for (int i = 0; i < files.length; i++)
				{
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
	}
}
