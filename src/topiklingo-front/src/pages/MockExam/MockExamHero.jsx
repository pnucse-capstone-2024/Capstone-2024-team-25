import CountUp from "react-countup";
import VisibilitySensor from "react-visibility-sensor";

import Container from "@components/common/container";

function MockExamHero({ title, description, applicant, candidate, passed}) {
  return (
    <div className="bg-gray-200 bg-opacity-50">
      <Container className="flex flex-wrap">
        <div className="flex items-center w-full">
          <div className="w-full">
            <h1 className="text-4xl font-bold leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight" data-aos="zoom-y-out" data-aos-delay="150">
              {title}
            </h1>
            <p className="py-3 text-xl text-gray-500 lg:text-xl xl:text-2xl" data-aos="zoom-y-out" data-aos-delay="200">
              {description}
            </p>
          </div>
        </div>
      </Container>
      <Container>
        <section className="text-gray-600 body-font">
          <div className="px-5 mx-auto -mt-10" data-aos="zoom-y-out" data-aos-delay="200">
          <p className="text-left title-font leading-relaxed mb-4">
                * As of 2023 / NIIED.
            </p>
            <div className="flex flex-wrap -m-4 text-center">
              <div className="sm:w-1/3 w-1/2">
                <h2 className="title-font font-medium sm:text-4xl text-3xl text-black">
                  <CountUp end={applicant} redraw>
                    {({ countUpRef, start }) => (
                      <VisibilitySensor onChange={start} delayedCall>
                        <span ref={countUpRef} />
                      </VisibilitySensor>
                    )}
                  </CountUp>
                </h2>
                <p className="leading-relaxed">Applicant</p>
              </div>
              <div className="sm:w-1/3 w-1/2">
                <h2 className="title-font font-medium sm:text-4xl text-3xl text-black">
                  <CountUp end={candidate} redraw>
                    {({ countUpRef, start }) => (
                      <VisibilitySensor onChange={start} delayedCall>
                        <span ref={countUpRef} />
                      </VisibilitySensor>
                    )}
                  </CountUp>
                </h2>
                <p className="leading-relaxed">Candidate</p>
              </div>
              <div className="sm:w-1/3 w-1/2">
                <h2 className="title-font font-medium sm:text-4xl text-3xl text-black">
                  <CountUp end={passed} redraw>
                    {({ countUpRef, start }) => (
                      <VisibilitySensor onChange={start} delayedCall>
                        <span ref={countUpRef} />
                      </VisibilitySensor>
                    )}
                  </CountUp>
                </h2>
                <p className="leading-relaxed">Passed</p>
              </div>
            </div>
          </div>
        </section>
      </Container>
    </div>
  );
}

export default MockExamHero;