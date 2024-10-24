/* eslint-disable react/destructuring-assignment */
import userOneImg from "@assets/user1.jpg";
import userTwoImg from "@assets/user2.jpg";
import userThreeImg from "@assets/user3.jpg";

import Container from "./container";

function Testimonials() {
  return (
    <Container>
      <div className="grid gap-10 lg:grid-cols-2 xl:grid-cols-3">
        <div className="lg:col-span-2 xl:col-auto hover:shadow-xl transform hover:-translate-y-2 transition duration-300">
          <div className="flex flex-col justify-between w-full h-full bg-gray-100 px-14 rounded-2xl py-14">
            <p className="text-2xl leading-normal ">
              &ldquo;The variety of tests and practice sessions have led to an <Mark>improvement</Mark> in my TOPIK scores,&rdquo; she shares. This platform has become an indispensable tool in her study regimen, providing her with the confidence and resources necessary to excel.
            </p>

            <Avatar
              image={userOneImg}
              name="Han"
              title="an International Student"
            />
          </div>
        </div>
        <div className="lg:col-span-2 xl:col-auto hover:shadow-xl transform hover:-translate-y-2 transition duration-300">
          <div className="flex flex-col justify-between w-full h-full bg-gray-100 px-14 rounded-2xl py-14">
            <p className="text-2xl leading-normal ">
            I Found it challenging to balance the demands of lesson planning and test creation. This platform has changed that. &ldquo;Being <Mark>free from the burden of creating tests</Mark> has allowed me to focus more on teaching,&rdquo; Cheol-su notes. He can now dedicate more time to interactive lessons and personalized student guidance.
            </p>

            <Avatar
              image={userTwoImg}
              name="Kim"
              title="a Korean Language Instructor"
            />
          </div>
        </div>
        <div className="lg:col-span-2 xl:col-auto hover:shadow-xl transform hover:-translate-y-2 transition duration-300">
          <div className="flex flex-col justify-between w-full h-full bg-gray-100 px-14 rounded-2xl py-14">
            <p className="text-2xl leading-normal ">
              &ldquo;The CBT offering has made <Mark>managing home study incredibly efficient</Mark>,&rdquo; Mang-gu states. This feature has streamlined the homework process, allowing for a more structured and effective home study routine.
            </p>

            <Avatar
              image={userThreeImg}
              name="Park"
              title="from a Korean Language Education Center"
            />
          </div>
        </div>
      </div>
    </Container>
  );
}

function Avatar(props) {
  return (
    <div className="flex items-center mt-8 space-x-3">
      <div className="flex-shrink-0 overflow-hidden rounded-full w-14 h-14">
        <img
          src={props.image}
          width="40"
          height="40"
          alt="Avatar"
        />
      </div>
      <div>
        <div className="text-lg font-medium">{props.name}</div>
        <div className="text-gray-600">{props.title}</div>
      </div>
    </div>
  );
}

function Mark(props) {
  return (
    <>
      {" "}
      <mark className="text-indigo-800 bg-indigo-100 rounded-md ring-indigo-100 ring-1">
        {props.children}
      </mark>{" "}
    </>
  );
}

export default Testimonials;