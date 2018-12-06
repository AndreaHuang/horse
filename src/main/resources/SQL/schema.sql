Create table racecard(
	rateDate char(8) not null,
	raceMeeting enum("ST","HV"),
	raceId varchar(3) not null,
	raceSeqOfDay TINYINT not null,
	raceClass TINYINT not null,
	distance SMALLINT not null,
	going enum("GOOD","GOOD TO FIRM","YIELDING","GOOD TO YIELDING","WET SLOW"),
	course varchar(30),

	draw TINYINT not null,
	horseId varchar(4),
	horseName varchar(30),
	hourseNo TINYINT not null,
        rating  TINYINT,
	jockey VARCHAR(20),
	trainer VARCHAR(20),
	addedWeight SMALLINT,
	declaredHorseWeight SMALLINT,
	lbsString VARCHAR(20),
	lbs FLOAT,
	winOdds FLOAT,

	place TINYINT,
	runningPosition VARCHAR(30),
	finishTimeString VARCHAR(12),
	finishTime FLOAT,
	cmment VARCHAR(200)
);
commit;


ALTER TABLE
   Racecard
MODIFY COLUMN going enum
       ("GOOD",
        "GOOD TO FIRM",
        "YIELDING",
        "GOOD TO YIELDING",
         "WET SLOW",
	"FAST")
AFTER distance;

Create table newRace(
	raceDate char(8) not null,
	raceMeeting enum("ST","HV"),
	raceSeqOfDay TINYINT not null,
	raceClass TINYINT not null,
	distance SMALLINT not null,
     going enum
       ("GOOD",
        "GOOD TO FIRM",
        "YIELDING",
        "GOOD TO YIELDING",
         "WET SLOW",
	"FAST"),
	course varchar(30),
	draw TINYINT not null,
	horseId varchar(4),
	horseName varchar(30),
	hourseNo TINYINT not null,
	rating  TINYINT,
    ratingDelta TINYINT,
	jockey VARCHAR(20),
	trainer VARCHAR(20),
	addedWeight SMALLINT,
	declaredHorseWeight SMALLINT,
	winOdds FLOAT
	
);

ALTER TABLE newrace RENAME column hourseNo to horseNo
ALTER TABLE racecard RENAME column ratedate to raceDate
