[global]
#De_stroy buffer cache
invalidate=1

#Not too small, not too large. We're time based anyway
size=4G

#Don't clutter logs per thread
per_job_logs=0

#Don't log too often
log_avg_msec=1000

#Try to avoid excessive syscalls
clocksource=gettimeofday

#Run for 60s even if you get finished early
runtime=60
time_based

#ioengine=sync
#ioengine=libaio
ioengine=${ioengine_temp!"libaio"}

#direct=1
direct=${direct_temp!"1"}

#iodepth=24
iodepth=${iodepth_temp!"16"}
#fsync_on_close=1
#fsync=1
#sync=1

#bs=4K
bs=${blocksize_temp!"4K"}

#Random Sequential write
rw=randwrite

${device_block}
