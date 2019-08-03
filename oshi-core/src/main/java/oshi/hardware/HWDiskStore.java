/**
 * OSHI (https://github.com/oshi/oshi)
 *
 * Copyright (c) 2010 - 2019 The OSHI Project Team:
 * https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package oshi.hardware;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oshi.SystemInfo;
import oshi.hardware.platform.linux.LinuxDisks;
import oshi.hardware.platform.mac.MacDisks;
import oshi.hardware.platform.unix.freebsd.FreeBsdDisks;
import oshi.hardware.platform.unix.solaris.SolarisDisks;
import oshi.hardware.platform.windows.WindowsDisks;

/**
 * A storage mechanism where data are recorded by various electronic, magnetic,
 * optical, or mechanical changes to a surface layer of one or more rotating
 * disks or or flash storage such as a removable or solid state drive. In
 * constrast to a File System, defining the way an Operating system uses the
 * storage, the Disk Store represents the hardware which a FileSystem uses for
 * its File Stores.
 */
public class HWDiskStore implements Serializable, Comparable<HWDiskStore> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(HWDiskStore.class);

    private String model = "";
    private String name = "";
    private String serial = "";
    private long size = 0L;
    private long reads = 0L;
    private long readBytes = 0L;
    private long writes = 0L;
    private long writeBytes = 0L;
    private long currentQueueLength = 0L;
    private long transferTime = 0L;
    private HWPartition[] partitions = new HWPartition[0];
    private long timeStamp = 0L;

    /**
     * Create an object with empty/default values
     */
    public HWDiskStore() {
    }

    /**
     * Copy constructor
     * 
     * @param diskStore
     *            The object to copy
     */
    public HWDiskStore(HWDiskStore diskStore) {
        HWPartition[] partsOrig = diskStore.getPartitions();
        HWPartition[] partsCopy = new HWPartition[partsOrig.length];
        for (int i = 0; i < partsOrig.length; i++) {
            partsCopy[i] = new HWPartition(partsOrig[i].getIdentification(), partsOrig[i].getName(),
                    partsOrig[i].getType(), partsOrig[i].getUuid(), partsOrig[i].getSize(), partsOrig[i].getMajor(),
                    partsOrig[i].getMinor(), partsOrig[i].getMountPoint());
        }
        this.name = diskStore.name;
        this.model = diskStore.model;
        this.serial = diskStore.serial;
        this.size = diskStore.size;
        this.reads = diskStore.reads;
        this.readBytes = diskStore.readBytes;
        this.writes = diskStore.writes;
        this.writeBytes = diskStore.writeBytes;
        this.currentQueueLength = diskStore.currentQueueLength;
        this.transferTime = diskStore.transferTime;
        this.partitions = partsCopy;
        this.timeStamp = diskStore.timeStamp;
    }

    /**
     * Make a best effort to update all the statistics about the drive without
     * needing to recreate the drive list. This method provides for more
     * frequent periodic updates of drive statistics. It will not detect if a
     * removable drive has been removed and replaced by a different drive in
     * between method calls.
     *
     * @return True if the update was (probably) successful, false if the disk
     *         was not found
     */
    public boolean updateAtrributes() {
        switch (SystemInfo.getCurrentPlatformEnum()) {
        case WINDOWS:
            return WindowsDisks.updateDiskStats(this);
        case LINUX:
            return LinuxDisks.updateDiskStats(this);
        case MACOSX:
            return MacDisks.updateDiskStats(this);
        case SOLARIS:
            return SolarisDisks.updateDiskStats(this);
        case FREEBSD:
            return FreeBsdDisks.updateDiskStats(this);
        default:
            LOG.error("Unsupported platform. No update performed.");
            break;
        }
        return false;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return this.model;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return this.serial;
    }

    /**
     * @return Get size of disk (in bytes)
     */
    public long getSize() {
        return this.size;
    }

    /**
     * @return the reads
     */
    public long getReads() {
        return this.reads;
    }

    /**
     * @return the bytes read
     */
    public long getReadBytes() {
        return this.readBytes;
    }

    /**
     * @return the writes
     */
    public long getWrites() {
        return this.writes;
    }

    /**
     * @return the bytes written
     */
    public long getWriteBytes() {
        return this.writeBytes;
    }

    /**
     * @return the length of the disk queue (#I/O's in progress). Includes I/O
     *         requests that have been issued to the device driver but have not
     *         yet completed. Not supported on macOS.
     */
    public long getCurrentQueueLength() {
        return this.currentQueueLength;
    }

    /**
     * @return the milliseconds spent reading or writing
     */
    public long getTransferTime() {
        return this.transferTime;
    }

    /**
     * @return Returns the partitions on this drive.
     */
    public HWPartition[] getPartitions() {
        return this.partitions;
    }

    /**
     * @return Returns the timeStamp.
     */
    public long getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(String model) {
        this.model = model == null ? "" : model;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial == null ? "" : serial;
    }

    /**
     * @param size
     *            Set size of disk (in bytes)
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @param reads
     *            the reads to set
     */
    public void setReads(long reads) {
        this.reads = reads;
    }

    /**
     * @param readBytes
     *            the bytes read to set
     */
    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }

    /**
     * @param writes
     *            the writes to set
     */
    public void setWrites(long writes) {
        this.writes = writes;
    }

    /**
     * @param writeBytes
     *            the bytes written to set
     */
    public void setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
    }

    /**
     * @param currentQueueLength
     *            the length of the disk queue (#I/O's in progress) to set
     */
    public void setCurrentQueueLength(long currentQueueLength) {
        this.currentQueueLength = currentQueueLength;
    }

    /**
     * @param transferTime
     *            milliseconds spent reading or writing to set
     */
    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    /**
     * @param partitions
     *            The partitions to set.
     */
    public void setPartitions(HWPartition[] partitions) {
        this.partitions = partitions;
    }

    /**
     * @param timeStamp
     *            The timeStamp to set.
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(HWDiskStore store) {
        // Naturally sort by device name
        return getName().compareTo(store.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.model == null ? 0 : this.model.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + (this.serial == null ? 0 : this.serial.hashCode());
        result = prime * result + (int) (this.size ^ this.size >>> 32);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HWDiskStore)) {
            return false;
        }
        HWDiskStore other = (HWDiskStore) obj;
        if (this.model == null) {
            if (other.model != null) {
                return false;
            }
        } else if (!this.model.equals(other.model)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.serial == null) {
            if (other.serial != null) {
                return false;
            }
        } else if (!this.serial.equals(other.serial)) {
            return false;
        }
        return this.size == other.size;
    }

}
