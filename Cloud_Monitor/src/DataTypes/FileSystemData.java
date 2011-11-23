package DataTypes;

import DataTypes.FileSystemData;

/*
 * Copyright (C) 2009-2010 School of Computer Science, University of St Andrews. All rights reserved.
 * Project Homepage: http://blogs.cs.st-andrews.ac.uk/h2o
 *
 * H2O is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * H2O is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with H2O.  If not, see <http://www.gnu.org/licenses/>.
 */

public class FileSystemData extends Data {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4820420240692464096L;

	public String machine_id; // only used when getting data from the database. can be null when data is added to the database.
	
	public String fileSystemLocation;
	public String fileSystemType;
	public String fileSystemName;

	public long fs_used; // Amount of space used in the file system (in Kilobytes)
	public long fs_free; // Amount of space free in the file system (in Kilobytes)
	public long fs_size; // Total size of file system (in Kilobytes)

	public long fs_files; // Total number of files in the file system

	public long fs_disk_reads; // Total number of disk reads
	public long fs_disk_read_bytes; // Total number of bytes read
	public long fs_disk_writes; // Total number of disk writes
	public long fs_disk_write_bytes; // Total number of bytes
	

	@Override
	public String toString() {
		return "FileSystemResults [file_System_Location=" + fileSystemLocation + ", file_System_Type=" + fileSystemType
				+ ", file_System_Name=" + fileSystemName + ", Fs_used=" + fs_used + ", Fs_free=" + fs_free + ", Fs_size=" + fs_size
				+ ", Fs_disk_reads=" + fs_disk_reads + ", Fs_files=" + fs_files + ", Fs_disk_read_bytes=" + fs_disk_read_bytes
				+ ", Fs_disk_writes=" + fs_disk_writes + ", Fs_disk_write_bytes=" + fs_disk_write_bytes + "]";
	}

	public FileSystemData min(FileSystemData other) {
		FileSystemData min = new FileSystemData();

		min.fileSystemLocation = other.fileSystemLocation;
		min.fileSystemType = other.fileSystemType;
		min.fileSystemName = other.fileSystemName;

		min.fs_size = fs_size;

		min.fs_used = this.fs_used < other.fs_used ? this.fs_used : other.fs_used;
		min.fs_free = this.fs_free < other.fs_free ? this.fs_free : other.fs_free;
		min.fs_disk_reads = this.fs_disk_reads < other.fs_disk_reads ? this.fs_disk_reads : other.fs_disk_reads;
		min.fs_files = this.fs_files < other.fs_files ? this.fs_files : other.fs_files;
		min.fs_disk_read_bytes = this.fs_disk_read_bytes < other.fs_disk_read_bytes ? this.fs_disk_read_bytes : other.fs_disk_read_bytes;
		min.fs_disk_writes = this.fs_disk_writes < other.fs_disk_writes ? this.fs_disk_writes : other.fs_disk_writes;
		min.fs_disk_write_bytes = this.fs_disk_write_bytes < other.fs_disk_write_bytes ? this.fs_disk_write_bytes
				: other.fs_disk_write_bytes;

		return min;
	}

	public FileSystemData max(FileSystemData other) {
		FileSystemData max = new FileSystemData();

		max.fileSystemLocation = other.fileSystemLocation;
		max.fileSystemType = other.fileSystemType;
		max.fileSystemName = other.fileSystemName;

		max.fs_size = fs_size;

		max.fs_used = this.fs_used > other.fs_used ? this.fs_used : other.fs_used;
		max.fs_free = this.fs_free > other.fs_free ? this.fs_free : other.fs_free;
		max.fs_disk_reads = this.fs_disk_reads > other.fs_disk_reads ? this.fs_disk_reads : other.fs_disk_reads;
		max.fs_files = this.fs_files > other.fs_files ? this.fs_files : other.fs_files;
		max.fs_disk_read_bytes = this.fs_disk_read_bytes > other.fs_disk_read_bytes ? this.fs_disk_read_bytes : other.fs_disk_read_bytes;
		max.fs_disk_writes = this.fs_disk_writes > other.fs_disk_writes ? this.fs_disk_writes : other.fs_disk_writes;
		max.fs_disk_write_bytes = this.fs_disk_write_bytes > other.fs_disk_write_bytes ? this.fs_disk_write_bytes
				: other.fs_disk_write_bytes;

		return max;
	}

	public FileSystemData sum(FileSystemData other) {
		FileSystemData total = new FileSystemData();

		total.fileSystemLocation = other.fileSystemLocation;
		total.fileSystemType = other.fileSystemType;
		total.fileSystemName = other.fileSystemName;

		total.fs_size = fs_size;

		total.fs_used = this.fs_used + other.fs_used;
		total.fs_free = this.fs_free + other.fs_free;
		total.fs_disk_reads = this.fs_disk_reads + other.fs_disk_reads;
		total.fs_files = this.fs_files + other.fs_files;
		total.fs_disk_read_bytes = this.fs_disk_read_bytes + other.fs_disk_read_bytes;
		total.fs_disk_writes = this.fs_disk_writes + other.fs_disk_writes;
		total.fs_disk_write_bytes = this.fs_disk_write_bytes + other.fs_disk_write_bytes;

		return total;
	}

	public FileSystemData convertToAverage(int numberOfMeasurements) {
		FileSystemData average = new FileSystemData();

		average.fileSystemLocation = fileSystemLocation;
		average.fileSystemType = fileSystemType;
		average.fileSystemName = fileSystemName;

		average.fs_size = fs_size;

		average.fs_used = this.fs_used / numberOfMeasurements;
		average.fs_free = this.fs_free / numberOfMeasurements;
		average.fs_disk_reads = this.fs_disk_reads / numberOfMeasurements;
		average.fs_files = this.fs_files / numberOfMeasurements;
		average.fs_disk_read_bytes = this.fs_disk_read_bytes / numberOfMeasurements;
		average.fs_disk_writes = this.fs_disk_writes / numberOfMeasurements;
		average.fs_disk_write_bytes = this.fs_disk_write_bytes / numberOfMeasurements;

		return average;
	}

	public FileSystemData adjustForBase(FileSystemData base) {

		FileSystemData adjusted = new FileSystemData();

		/*
		 * Not Adjusted.
		 */
		adjusted.fileSystemLocation = fileSystemLocation;
		adjusted.fileSystemType = fileSystemType;
		adjusted.fileSystemName = fileSystemName;
		adjusted.fs_size = fs_size;
		adjusted.fs_used = this.fs_used;
		adjusted.fs_free = this.fs_free;
		
		
		/*
		 * Adjusted.
		 */
		adjusted.fs_files = this.fs_files - base.fs_files;

		adjusted.fs_disk_reads = this.fs_disk_reads - base.fs_disk_reads;

		adjusted.fs_disk_read_bytes = this.fs_disk_read_bytes - base.fs_disk_read_bytes;
		adjusted.fs_disk_writes = this.fs_disk_writes - base.fs_disk_writes;
		adjusted.fs_disk_write_bytes = this.fs_disk_write_bytes - base.fs_disk_write_bytes;

		return adjusted;
	}

	public boolean needsAdjustedForBase() {
		return true;
	}

	public FileSystemData getNewInstance() {
		return new FileSystemData();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fileSystemLocation == null ? 0 : fileSystemLocation.hashCode());
		result = prime * result + (fileSystemName == null ? 0 : fileSystemName.hashCode());
		result = prime * result + (fileSystemType == null ? 0 : fileSystemType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileSystemData other = (FileSystemData) obj;
		if (fileSystemLocation == null) {
			if (other.fileSystemLocation != null)
				return false;
		} else if (!fileSystemLocation.equals(other.fileSystemLocation))
			return false;
		if (fileSystemName == null) {
			if (other.fileSystemName != null)
				return false;
		} else if (!fileSystemName.equals(other.fileSystemName))
			return false;
		if (fileSystemType == null) {
			if (other.fileSystemType != null)
				return false;
		} else if (!fileSystemType.equals(other.fileSystemType))
			return false;
		return true;
	}

}

