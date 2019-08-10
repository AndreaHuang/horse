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

alter table racecard add column propByWinOdds float;

alter table racecard add column jockeyTtlCnt int;
alter table racecard add column jockeyPosCnt int;
alter table racecard add column jockeyFx float;

alter table racecard add column jockeyTtlCnt_Distance int;
alter table racecard add column jockeyPosCnt_Distance int;
alter table racecard add column jockeyFx_Distance float;

alter table racecard add column horseTtlCnt int;
alter table racecard add column horsePosCnt int;
alter table racecard add column horseFx float;

alter table racecard add column horseTtlCnt_Distance int;
alter table racecard add column horsePosCnt_Distance int;
alter table racecard add column horseFx_Distance float;
alter table racecard add column predicted_date char(14);
create table racecarddraw(
  racemeeting char(2) not null,
   distance smallint not null,
   draw smallint not null,
    ttlCount smallint not null,
    posCount smallint not null,
    fx float
);
alter table racecarddraw add column course varchar(30);
alter table racecard add column drawFx float;

 create table survival_analysis(
 racedate char(8) not null,
 seq TINYINT not null,
 place enum("ST","HV"),
 distance smallint not null,
 course varchar(30),
 horseNoA TINYINT not null,
 horseNoB TINYINT not null,
 horseIdA varchar(4),
 horseIdB varchar(4),
 drawA TINYINT not null,
 drawB TINYINT not null,
 jockeyA VARCHAR(20),
 jockeyB VARCHAR(20),
 jockeyACnt smallint,
 jockeyBCnt smallint,
 jockeyRelRiskA2B float,
 drawACnt smallint,
 drawBCnt smallint,
 drawRelRiskA2B float,
 sameHorseACnt smallint,
 sameHorseBCnt smallint,
 sameHorseRelRiskA2B float,
 diffHorseACnt smallint,
 diffHorseBCnt smallint,
 diffHorseRelRiskA2B float ,
 result smallint not null);

 alter table survival_analysis add raceMeeting enum("ST","HV");
  alter table survival_analysis add finishTimeA  float;
 alter table survival_analysis add finishTimeB  float;

 alter table survival_analysis add predicted_result smallint;
 alter table survival_analysis add predicted_finishTime float;
 alter table survival_analysis add predicted_date  char(14);
 alter table survival_analysis add raceClass TINYINT not null;
