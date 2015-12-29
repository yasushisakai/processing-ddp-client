/*
* (c)Copyright 2013-2014 Ken Yee, KEY Enterprise Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* Modifications Copyright (C) 2015 Yasushi Sakai
* Modifications based upon the usage inside a Processing sketch
* - changed package name
* - added member PApplet variable
* - deleted slf4j Logging, and use println for inspection
* - deleted JUnit test
* - added functions dispose
*/

package ddpclient;

/**
 * For sending Meteor a username/password for authentication
 * @author kenyee
 */
public class EmailAuth extends PasswordAuth
{
    public EmailAuth(String email, String pw) {
        super(pw);
        assert(email != null);
        this.user.put("email", email);
    }
}
