package edu.eckerd.google.api.services.calendar

import edu.eckerd.google.api.services.calendar.models.Event
import edu.eckerd.google.api.language.JavaConverters._

import scala.collection.JavaConverters._
import java.time.ZonedDateTime

class events(calendar: Calendar) {
  val service = calendar.asJava

  def list: List[Event] = {

    val events = service.events.list("primary")
      .setMaxResults(500)
      .setTimeMin(ZonedDateTime.now().asGoogle)
      .setOrderBy("startTime")
      .setSingleEvents(true)
      .execute()
      .getItems


    events.asScala.toList.map(_.asScala)
  }

  def create(event: Event, calendar: String = "primary", sendNotifications: Boolean = true): Event = {
    service.events()
      .insert(calendar, event.asJava)
      .setSendNotifications(sendNotifications)
      .execute()
      .asScala
  }

}
