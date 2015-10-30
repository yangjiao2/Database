/*COPY "Persiann"."precipObject"(object_id, latitude, longitude, time, intensity) FROM '/Applications/persiann/precipObject_list_001.txt' USING DELIMITERS ',';
SELECT * FROM "Persiann"."precipObject" LIMIT 10;
*/
/*
COPY "Persiann"."precipObject_stats" (
  object_id, 
  object_avg_intensity, 
  object_median_intensity, 
  object_stddev_intensity, 
  object_max_intensity,
  object_cent_lat,
  object_cent_lon,
  object_volume,
  object_duration ,
  object_speed ,
  "object_startTime" ,
  "object_endTime" ,
  object_start_lat_centroid ,
  object_start_lon_centroid ,
  object_end_lat_centroid ,
  object_end_lon_centroid) FROM '/Applications/persiann/precipObject_stats.csv' USING DELIMITERS ',' CSV;
SELECT * FROM "Persiann"."precipObject_stats" LIMIT 10;
*/



/*
INSERT INTO "Persiann"."precipObject_info"


SELECT  
  "precipObject"."Id",
  "precipObject".object_id, 
  "precipObject".latitude, 
  "precipObject".longitude, 
  "precipObject"."time", 
  "precipObject".intensity,
  "precipObject_stats".object_avg_intensity, 
  "precipObject_stats".object_median_intensity, 
  "precipObject_stats".object_stddev_intensity, 
  "precipObject_stats".object_max_intensity, 
  "precipObject_stats".object_cent_lat, 
  "precipObject_stats".object_cent_lon, 
  "precipObject_stats".object_volume, 
  "precipObject_stats".object_duration, 
  "precipObject_stats".object_speed, 
  "precipObject_stats"."object_startTime", 
  "precipObject_stats"."object_endTime", 
  "precipObject_stats".object_start_lat_centroid, 
  "precipObject_stats".object_start_lon_centroid, 
  "precipObject_stats".object_end_lat_centroid, 
  "precipObject_stats".object_end_lon_centroid

FROM 
  "Persiann"."precipObject" LEFT OUTER JOIN "Persiann"."precipObject_stats"
ON 
  "precipObject".object_id = "precipObject_stats".object_id
ORDER BY object_id ASC;
*/
/*
EXPLAIN ANALYZE
*/
/*
SELECT * FROM "Persiann"."precipObject_info" WHERE intensity > 100;
*/

/*
SELECT * FROM "Persiann"."precipObject_info" AS poi
WHERE poi.id IN 
(SELECT po."Id" FROM "Persiann"."precipObject" AS po WHERE po.intensity > 100);
*/
/*
CREATE INDEX idx_intensity
  ON "Persiann"."precipObject_info"
  USING btree
  (intensity);
*/

SELECT * FROM "Persiann"."precipObject_info" WHERE intensity > 100;
