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

ALTER TABLE newrace RENAME column hourseNo to horseNo;

ALTER TABLE racecard RENAME column hourseNo to horseNo;
ALTER TABLE racecard RENAME column ratedate to raceDate;

ALTER TABLE racecard add column horse_winPer FLOAT;
ALTER TABLE racecard add column jockey_winPer FLOAT;
ALTER TABLE racecard add column horse_winCount SMALLINT;
ALTER TABLE racecard add column jockey_winCount SMALLINT;
ALTER TABLE racecard add column horse_newHorse SMALLINT;
ALTER TABLE racecard add column horse_newDistance SMALLINT;
/*Statistic for new Race*/
ALTER TABLE newrace add column horse_winPer FLOAT;
ALTER TABLE newrace add column jockey_winPer FLOAT;
ALTER TABLE newrace add column horse_winCount SMALLINT;
ALTER TABLE newrace add column jockey_winCount SMALLINT;
ALTER TABLE newrace add column horse_newHorse SMALLINT;
ALTER TABLE newrace add column horse_newDistance SMALLINT;

create table racestats (
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
	avg_finishTime FLOAT,
	min_finishTime FLOAT,
    count SMALLINT not null
)

alter table newrace add column horse_last4SpeedRate SMALLINT;
alter table newrace add column horse_latestSpeedRate SMALLINT;
alter table newrace add column days_from_lastRace SMALLINT;


alter table racecard add column horse_last4SpeedRate SMALLINT;
alter table racecard add column horse_latestSpeedRate SMALLINT;
alter table racecard add column days_from_lastRace SMALLINT;
alter table newrace add column finishTime_predicted FLOAT;
alter table racecard add column finishTime_predicted FLOAT;

alter table newrace add column predicted_place SMALLINT;
alter table racecard add column predicted_place SMALLINT;

ALTER TABLE newrace RENAME column finishTime_predicted to predicted_finishTime;
ALTER TABLE racecard RENAME column finishTime_predicted to predicted_finishTime;

create table dividend (
raceDate char(8) not null,
raceSeqOfDay TINYINT not null,
    pool varchar(30),
    winning varchar(50),
    dividend FLOAT
)
alter table racecard add column weightRadio float;
alter table racecard add column weightRD float;