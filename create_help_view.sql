create table schedule_history
as
(
select * from (select 0 as way, dcourse.loid as course, days.delaysec as delay, days.orderincourse as order, days.scheduleddeparture as scheduled, days.realdeparture, line.name as line_name, line.loid as lineid, stop.name as stop, stop.loid as stopid from csipdaystopping days
join csipline as line
	on line.loid = days.line_loid
join csipstoppoint as stop
	on stop.loid = days.stoppoint_loid
join (select * from csipdaycourse) as dcourse
	on dcourse.loid = days.daycourse_loid
where line.number = 21
order by (dcourse.loid, days.orderincourse)) as orig 
where exists 
(select * from csipdaystopping ds2 where orderincourse = 0 and stoppoint_loid = 371 and line_loid = orig.lineid and
ds2.daycourse_loid = orig.course)

UNION

select * from (select 1 as way, dcourse.loid as course, days.delaysec as delay, days.orderincourse as order, days.scheduleddeparture as scheduled, days.realdeparture, line.name as line_name, line.loid as lineid, stop.name as stop, stop.loid as stopid from csipdaystopping days
join csipline as line
	on line.loid = days.line_loid
join csipstoppoint as stop
	on stop.loid = days.stoppoint_loid
join (select * from csipdaycourse) as dcourse
	on dcourse.loid = days.daycourse_loid
where line.number = 21
order by (dcourse.loid, days.orderincourse)) as orig 
where exists 
(select * from csipdaystopping ds2 where orderincourse = 0 and stoppoint_loid = 103 and line_loid = orig.lineid and
ds2.daycourse_loid = orig.course)


UNION

select * from (select 0 as way, dcourse.loid as course, days.delaysec as delay, days.orderincourse as order, days.scheduleddeparture as scheduled, days.realdeparture, line.name as line_name, line.loid as lineid, stop.name as stop, stop.loid as stopid from csipdaystopping days
join csipline as line
	on line.loid = days.line_loid
join csipstoppoint as stop
	on stop.loid = days.stoppoint_loid
join (select * from csipdaycourse) as dcourse
	on dcourse.loid = days.daycourse_loid
where line.number = 15
order by (dcourse.loid, days.orderincourse)) as orig 
where exists 
(select * from csipdaystopping ds2 where orderincourse = 0 and stoppoint_loid = 182 and line_loid = orig.lineid and
ds2.daycourse_loid = orig.course)

UNION

select * from (select 1 as way, dcourse.loid as course, days.delaysec as delay, days.orderincourse as order, days.scheduleddeparture as scheduled, days.realdeparture, line.name as line_name, line.loid as lineid, stop.name as stop, stop.loid as stopid from csipdaystopping days
join csipline as line
	on line.loid = days.line_loid
join csipstoppoint as stop
	on stop.loid = days.stoppoint_loid
join (select * from csipdaycourse) as dcourse
	on dcourse.loid = days.daycourse_loid
where line.number = 15
order by (dcourse.loid, days.orderincourse)) as orig 
where exists 
(select * from csipdaystopping ds2 where orderincourse = 0 and stoppoint_loid = 33 and line_loid = orig.lineid and
ds2.daycourse_loid = orig.course)
);
