Feedback for UI Design (C. Loomis + comments from Meb)
==================================

General Comments
----------------

The new UI layout and design is overall excellent.  The design is much
cleaner and more polished than the existing one.  The uniformity in
the layout should make using the application more intuitive and
straightforward.

### Responsive Design

The current layout/stylesheet is not responsive at all.  

> Meb: in general or only on smart devices?

It generally works fine in a computer's browser, but doesn't
scale/flow reasonably when viewed on tablet/smartphone screen sizes.
This is something that we should try to do as the application is
likely to be used (at least minimally) from such devices.  Firefox has
a nice "Responsive Design View" feature to see what the layout looks
like on the various devices.

> Meb: I didn't consider smart devices in this design. For this, I would 
use JQuery UI Mobile (a different incarnation of JQuery UI, and incompatible) 
which has similar widgets. We would reuse the same JS dynamic behaviour, with 
a specific mobile randering.

> Cal: I don't think that we necessarily need to use different
  widgets.  The only things were using are the accordian and buttons,
  both of which have a high chance of working on smaller devices.  The
  real issue is the fixed width of the layout.  This doesn't adapt at
  all to various sizes (even on a computer).  Making the layout width
  variable would go a long way toward making the application
  accessible from smaller devices.

### Banner

I think that the banner takes up too much screen real estate for the
generally small amount of information it conveys.  Perhaps about
two-thirds of its current size would be a reasonable compromise.  In
reducing the size, perhaps the third level of text can be removed.
This level of detail is probably appropriate as an additional drop
down panel or as a tooltip.

> Meb: we can easily play with this

Regarding the icons in the banners, this is not completely consistent
between the various pages.  It would be more effective if an image
were used for all pages.  (Ideas for this in the comments for
individual pages.)

> Meb: the icons are rough placeholders, as I ran out of steam and ideas. 
I also didn't want to stall the work for this. And yes we need to pick a 
consistent theme for icons, which includes more than the banner*

> Cal: I've selected a set of icons from the Font Awesome collection.
  A list of the icons and their meanings are in the icon-list.html
  file.  Generally the mapping between the icons and functions are
  good, although there are a few that are a stretch.  There are plenty
  of others in the collection if someone else wants to try an
  alternate mapping. 

### Breadcrumbs

The breadcrumbs above the accordian are really not useful.  They
simply duplicate information that is in the banner (and title) on most
pages.  The only link that is important that isn't elsewhere is the
"home icon".  This could easily be moved up to the header line along
with the other major category links.

So the changes I'd suggest for this is:
  * Remove breadcrumb line above accordian.
  * Add "home icon" to the top red menu line.
  * Move top-level buttons from right to left side.
  * Make resource name elements in banner active links.
  * Use only generic name or last element in resource name in title.

> Meb: I'm not sure I agree with the suppression of the breadcrumbs. 
At least not until we have a valid replacement. 
The breadcrumbs are the main back navigation mechanism. We can't use 
links in the banner, as some are to long and have to be trimmed. We 
could introduce a different scheme, but this is not clear to me yet.
The latest Jenkins UI has drop down on links, but I find them clunky.

### Footer

I don't think that we really benefit much from having the "swiss make
software" link in the footer.  This is probably something that would
be better as a panel of the login page, if it isn't removed entirely. 

> Meb: I had great feedback from showing this. We might want to
discuss this a but more.

There is really no reason to use a bit.ly link to the "swiss made
software" site.  If this is kept, the full link should be used.

> Meb: there is since we need to have all links using https and
the logo is natively is only available via http. We could have a
local copy of the logo to avoid it, but the link remains a problem.

> Cal: The page elements need to be served from secure links.  The
logo should definitely be linked from a local copy within the
application.  This looks to be the case already.  I don't think that
there isn't any need for external links to all be secure.  The bit.ly
link only serves to obscure the final destination.  I've changed the
templates to use the direct address; this can be changed back if it
really turns out to be a problem.

I think that the footer would look better having the SixSq logo
separate the copyright from the version number.  Making it slightly
larger and putting it in red would emphasize it.  It should also be an
active link to the SixSq website.

> Meb: we can play with this indeed. But I like the subtle grey SixSq
logo.

> Cal: I've separated the logo from the copyright block and added a
link to the SixSq website.  I've moved the logo to the left for now,
but this can be changed to any order we like.

### Accordian

The accordian layout is generally good, although I think it would be
further improved with:
  * Having more legible open/close boxes for the sections.  I find the
  current ones almost impossible to see the state.  It also isn't
  obvious that they are clickable.  May highlight them in green like
  internal buttons? 

> Meb: yes we can improve the up/down arrow layout

  * Add an open all/close all icon to the accordion.  This allows
  people to see all of the information by scrolling without having to
  individually open up each accordian section.

> Meb: opening all accordions goes against its philosophy, which is
that only one can be open at a time. If two
things should be opened at the same time, then we might want to
move them in the same section and separate them by inner tabs.

> Cal: Moving it to tabs doesn't solve the issue.  I'd like to have
  all of the sections visible at the same time.  (E.g. wanting to
  print the page of the configuration.)  If this can't be done, it's
  not critical, but I suspect I won't be the only one to ask for
  this. 

  * Internal tabs are not visually distinct from the accordian
  headers.  It would help if they were a different color.  Maybe a
  slightly lighter shade of gray?

> Meb: we have the red tab theme that I'm not using. I'll play with
that.

  * Buttons at the end of an accordian section are often clipped by
  the next section header.  More vertical space should be added to
  ensure this doesn't happen.

> Meb: yes.


### Summary Sections for Views

The summary section for views of resources is not very useful.  It
would be better if this were simply merged into the banner.  (Most of
the information is already there.)  The owner is better in the
authorization section and the create/modified times could be added to
the banner, ideally in "... hours/days ago" format.  

> Meb: yes the summary is sort of a catch-all section. Once the info
is conveniently available from somewhere else, we can remove it.


### Top Red Menu Line

This would be improved by using icons for the links rather than text.
A good model is the top line of the GitHub pages which use tooltips to
explain each icon.  (Add the 'home' icon here as discussed before.)

> Meb: mmmm... if we find the right icons. I actually find GitHub icons
not intuitive. But if we do... great!

### Icons

I really don't find the brushed aluminum icons with letters very
appealing.  Finding appropriate graphical icons would be much more
effective for these icons and also for the various menu items.

[Entypo](http://www.entypo.com) provides a rich set of icons that can
be used as characters in a font.  [Font
Awesome](http://fortawesome.github.io/Font-Awesome/) is also another
possibility; this one also allows the font to be subsetted to minimize
the download size for the user.

> Meb: as I said earlier, these are place holders and we should
replace them with real ones. But I think they look good enough
such that we can go live with them if we haven't found a suitable
replacement by then.

> Cal: I've removed them and replaced them with smaller icons from
  Font Awesome.  The same icons are used consistently in the menu,
  banners, and in buttons. 

Login Page
----------

  * Ensure that the 'trade' entity is used consistently everywhere.
    (This has been pushed to repository.)
  * Change 'email' to 'Email' in registration section.
  * Find an appropriate icon for each panel of the banner.  Perhaps
    the SixSq logo for the welcome panel? 

> Meb: I'm not sure we need an icon in each banner. But I'll play
with the SixSq logo... good idea. However, for the Login page
which has an animated banner, I don't think a mandatory icon is
required, as per the SixSq website.

> Cal: I've updated the capitalization for email.

Welcome Page
------------

Although I generally favor having fewer pages, I think that this page
is trying to do too many different things.  Specifically, the
consumption and metering functions would be better off on a separate
page.

I'd like to see the various "Modules" sections reduced to a single
section with perhaps additional column(s) to distinguish the various
types rather than separate sections.  It may be worthwhile to make
this an "active" table where the columns can be sorted or filtered.
The modules section should be the first on the page. 

> Meb: yes, this page has 'grown' into a bit of a monster. And I
would indeed welcome a discussion on metering and co.

> Meb: the separation between the different module sections though are
I think useful. I think with a real system this will become clearer
(published, etc)

Remove duplication in table between "Category" and the category icon
(as you've mentioned in the README).

The "Deployments, Runs, etc." section would be the only other section
on this page.  In this section:
  * Could this section be renamed to something shorter like "Current
  Activity"? 
  * What is the difference between a deployment and a simple run?  Is
  this distinction important? 
  
> Meb: yes it is. A simple run is a unique VM instantiation, which
is useful when wanting to test a single image. No orchestrator is
involved in this case. But it's not a very popular feature.
  
  * Can the tabs be replaced with a column giving the type of
  activity?  Making this an active table would then allow people to
  easily find what they are looking for.

> Meb: I think the real feature here is search. Which we need to explore.
  
  * Replace full UUID with just first 5 characters with the full UUID
  available as a tooltip. 
  
> Meb: yes, good idea.

In the banner, an appropriate icon would be the "home" icon.  To make
the banner useful, a summary of the current activity could be added
(e.g. number of deployments, VMs, etc.).

> Meb: I'll see if I can somehow merge the columns with the banner.

> Cal: I've added the home icon to the banner, inlined with the
  header.

Is the "New Project" button as a top-level button appropriate?  It
really is only linked to the "Modules" section.

> Meb: yes, but it's a common action that should not be buried too
deep.

Consumption and Metering
------------------------

As stated above, I think this would be better on a dedicated page with
a link provided from the Top Red Menu Line. 

I think that we'll probably need a face-to-face discussion on how to
make the presentation of the consumption and metering effective (also
probably real data to see!).  My biggest problem with the current
layout is that it stacks things that have different units making the
total height of the bars/plots meaningless.

Also the units are not correct.  CPU will need to be measured in
SpecInts (or similar), RAM in GB-hours, and Storage in GB-hours.

> Meb: this part is only a initial thought and will not be part of
the first release. And yes we should talk about it. It will also
require server-side development.

> Cal: I've moved this information to a separate page:
  utilization.html. 

Project View Page
-----------------

Could the number of buttons be reduced by having a "New..." drop down
list with "Project", "Machine Image", and "Deployment" as options?
This would remove some visual clutter.  For consistency, the same
could be used on the "Welcome" page with "Machine Image" and
"Deployment" deactivated.

> Meb: this is what we had in the alpha version. But users told us
that they preferred buttons for 'common actions' like module creation.
But we can introduce a 'Other' button that has a drop-down feature.
We can then move actions easily.

I'd suggest that the title of this page should be the last element of
the resource; "CoolProject" in this case. 

> Meb: yes, or the last two. Currently, we have: Public/BaseImages/Fedora/14.0,
where the '14.0' is the version of Fedora, not the SlipStream module version.
In this case showing the last part of the URI is meaningless. But is this the
right pattern?

Remove duplication in table between "Category" and the category icon.

> Meb: yes.

Project Edit Page
-----------------

No additional comments.


Image View Page
---------------

The word "overview" is confusing as this doesn't provide an overview
of the given image, but of the relationships between this images and
others.

> Meb: yes. 'Relationships' then?

Graphically, this would be clearer as a hierarchy if arrows were used
to show the relationships.  The machine icon is confusing as these are
not really virtual machines (yet).

> Meb: here as well, the icon is a place holder. We need to convey
an 'image' icon. Any idea?

The interesting part of the resource names are at the end of the
names.  It would be better to cut the beginning of the resource name
rather than the middle.  Provide the full resource name in the tooltip
(which is currently empty). 

> Meb: yes

There is too much whitespace around the graphic and clicking on the
children moves the parent outside of the section boundary. 

> Meb: yes

Same comment about UUIDs in table as above. 

Title of page should again be last (last two?) elements of the
resource name.

> Meb: yes


Image Edit Page
---------------

For "Reference" section, change "Is base ..." to just "Base Image?".

> Meb: yes

Put the comment into a help bubble.

> Meb: yes

For the "Creation Recipes" section, it would be better to have
separate tabs for the "Recipe" and "Prerecipe".  

> Meb: yes

Deployment View Page
--------------------

I find it confusing to mix both nodes (virtual machines) with machine
images in the same diagram.  I would leave the details of the images
to the node definition section and not try to add this here.  It might
be useful to add parameter mapping if this can be done without making
the diagram too busy.

> Meb: when giving demos, or trying to understand a deployment, we
almost always navigate: node -> image. So I think putting it on the
same diagram is right. But having clearer icons should clarify the
difference between node and image. The same distinction between node
and VM is required for the deployment diagram.

See previous comments about the graphic layout and labels.


Deployment Edit Page
--------------------

No additional comments.


Versions/History
----------------

Title should be resource id (or 'Versions'?).

> Meb: yes

Eventually when we have a textual format for the resource, allowing
users to display the differences between versions, rather than relying
on the comment, would be helpful.

> Meb: indeed. Both would be good.

Run
---

The tooltips for the state machine boxes take the value of whatever
box was last hovered over.

> Meb: I had to lobotomize this part... it already works in prod.

See previous comments about the graphic in previous sections.

From the example, I'm not sure what information would be in each node
section.  Why is there a summary tab?  What information is in 'General
Runtime Parameters'? 

> Meb: I was trying to convey the idea that we can separate the
'boilerplate' parameters from the 'custom/user' and 'standard' (e.g.
ip, id). A more meaningful default would do a better job.

Title and banner should be consistent.  If the deployment rather than
the run UUID will be used, then that should also be the part in blue
in the banner; the UUID can then be the subtitle in the banner.

> Meb: yes. We should also add a 'tag' that users can give to a deployment
and give it a prominent place in the banner.

Why is terminate button not duplicated at the bottom of the page? 

> Meb: no good reason :-P

Logout Page
-----------

Not in the repository.

Is a page actually needed?  Should it just clear the session and
redirect to the login page?  Presumably the logout link (icon) will
bring up a confirmation dialog.

> Meb: I think we need both. An overlay dialog for logging-out and
a static page for when the user is not already on a page.


Configuration Page
------------------

Presumably this is a super user only page that would be accessed
through a link in the Top Red Menu Line?  

> Meb: correct.

Shouldn't the title and resource name be "Configuration"? 

> Meb: yes

Is there more than one "Configuration"?  If not, it should be
"Configuration" in the Top Red Menu Line.  (Even better, just an
icon.  Same for "Users".)

> Meb: yes and yes


Documentation Page
------------------

This is one page that does not benefit at all from the accordian
layout.  This content would be more visible and shorter if just
formatted as a simple table or list. 

> Meb: yes this page sucks.  I'll try that.

Why are there "Save" and "Reload Configuration File" buttons on this
page? 

> Meb: they shouldn't

Title should be "Documentation".

> Meb: yes

Why is the icon labeled with 'C'?

> Meb: I'm missing the 'C' icon. Will replace it when I get it.

We should also add an 'Acknowledgements' section that identifies other
open source software that we use.

> Meb: yes indeed.


Error Page
----------

I would add the warning icon as part of the banner and provide the
text of the error message in the banner subtitle.  There is no need
for a separate error section on the page.

> Meb: I tried that but this like the results. I'll put it back and
we can have a look.

The title should be "Error", not "Login". 

> Meb: yes

Will the standard layout also provide a slot for an error message when
editing resources?  Or will the user always need to click the 'back'
button to get back to the editing form? 

> Meb: each page will have a hidden 'Error' div ready to host the
dynamic error message. This page is for things go really badly :-P

Users Page
----------

I think that the primary focus of this page--user resource
consumption--is wrong.  The superuser is much more likely to be
interested in managing the users of the service.  As such this page
should concentrate on giving the registration details of the users and
to provide the ability to search for a given user, suspend a user,
delete a user, and create new users.

I don't think that the accordian layout is particularly adapted to
listing users and providing this information concisely.  Although, you
can mimic the accordian layout to provide a "Users" header as you've
done with this page for visual consistency. 

An active table that allows paging of users, sorting of results by
column, search on column values, and selecting individual users for
suspension/removal would be more intuitive and more useful.

That said, the table could also include some information about
resource consumption (numbers of active deployments, VMs, etc.) for
the individual users.

The superuser is also likely to ask questions about resource use but
concentrating on **relative** use between users.  This need is
sufficiently different from user management concerns that it should be
on a separate page.

The questions that are likely to be asked by the superuser are:  Who's
has the largest number of deployments over the last day/week/month?,
Who's started the largest number of VMs?, etc.  Answering these
questions require comparision between the users for which the current
graphics are poorly adapted.  I suspect that a better model for this
would be a heat map, allowing the superuser to make high-level
comparisions for a number of resources and to then drill down to get
more information. 

> Meb: this page is also immature and thrown in for discussion. We
got feedback from MSPs that want to manage different users, where
their individual consumption is important. But I think you're proposing
enough interesting ideas for me to have another go at the page. We
can then discuss. This page is also related to the way we present
metering and how we can create summaries (and cross users with something
like a head map).
